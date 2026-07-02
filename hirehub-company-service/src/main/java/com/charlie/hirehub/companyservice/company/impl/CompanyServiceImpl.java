package com.charlie.hirehub.companyservice.company.impl;


import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.CompanyRepository;
import com.charlie.hirehub.companyservice.company.CompanyService;
import com.charlie.hirehub.companyservice.company.dto.request.CreateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.request.UpdateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;
import com.charlie.hirehub.companyservice.company.exception.CompanyHasDependencyException;
import com.charlie.hirehub.companyservice.company.exception.CompanyNotFoundException;
import com.charlie.hirehub.companyservice.company.exception.TooManyRequestsException;
import com.charlie.hirehub.companyservice.company.integration.JobClientService;
import com.charlie.hirehub.companyservice.company.integration.ReviewClientService;
import com.charlie.hirehub.companyservice.company.mapper.CompanyMapper;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepo;
    private final JobClientService jobClientService;
    private final ReviewClientService reviewClientService;

    private static final Logger logger =
            LoggerFactory.getLogger(CompanyServiceImpl.class);

    public CompanyServiceImpl(CompanyRepository companyRepo, JobClientService jobClientService, ReviewClientService reviewClientService){
        this.companyRepo = companyRepo;
        this.jobClientService = jobClientService;
        this.reviewClientService = reviewClientService;
    }

    @Override
    @RateLimiter(name = "readCompanyRateLimiter", fallbackMethod = "findAllCompaniesRateLimitFallback")
    public List<CompanyDTO> findAllCompanies() {

        logger.info("Fetching all Companies");

        List<Company> companies = companyRepo.findAll();

        List<CompanyDTO> companyDTOs = companies.stream()
                .map(CompanyMapper::toCompanyDTO)
                .toList();

        logger.info("Fetched {} companies successfully", companyDTOs.size());

        return companyDTOs;
    }

    @Override
    @RateLimiter(name = "readCompanyRateLimiter", fallbackMethod = "findCompanyByIdRateLimitFallback")
    public CompanyDTO findCompanyById(Long id) {

        logger.info("Fetching company with id: {}", id);

        Company company = companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company with id: " + id + " does not exist"));

        logger.info("Successfully fetched company with id: {}", id);
        return CompanyMapper.toCompanyDTO(company);
    }

    @Override
    @RateLimiter(name = "writeCompanyRateLimiter", fallbackMethod = "createCompanyRateLimitFallback")
    public CompanyDTO createCompany(CreateCompanyRequest companyRequest) {

        logger.info("Creating a company with name: {}", companyRequest.getName());

        Company company = CompanyMapper.toCompany(companyRequest);
        Company savedCompany = companyRepo.save(company);

        logger.info("Successfully created {} company", savedCompany.getName());

        return CompanyMapper.toCompanyDTO(savedCompany);
    }

    @Override
    @RateLimiter(name = "writeCompanyRateLimiter", fallbackMethod = "deleteCompanyByIdRateLimitFallback")
    public void deleteCompanyById(Long id) {

        logger.info("Deleting company with id {}", id);

        Company company = companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company with id " + id + " not found"));

        //Search if company has jobs and reviews for itself
        if(jobClientService.jobWithCompanyIdExists(id)){
            logger.warn("Can't delete company with id {} because it has Jobs", id);
            throw new CompanyHasDependencyException("Cannot delete company because Jobs exist for this company.");
        }

        if(reviewClientService.reviewsExistsByCompanyId(id)){
            logger.warn("Can't delete company with id {} because it has Reviews", id);
            throw new CompanyHasDependencyException("Cannot delete company because Reviews exist for this company.");
        }

        logger.info("No Jobs or Reviews present for company with id {}", id);

        companyRepo.delete(company);

        logger.info("Successfully deleted company with id {}", id);
    }

    @Override
    @RateLimiter(name = "writeCompanyRateLimiter", fallbackMethod = "updateCompanyByIdRateLimitFallback")
    public CompanyDTO updateCompanyById(Long id, UpdateCompanyRequest updateCompanyRequest) {

        logger.info("Updating company with id {}", id);

        Company updatedCompany = CompanyMapper.toCompany(updateCompanyRequest);

        Company currentCompany = companyRepo.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + id +" not found"));

        currentCompany.setName(updatedCompany.getName());
        currentCompany.setDescription(updatedCompany.getDescription());
        Company savedUpdatedCompany = companyRepo.save(currentCompany);

        logger.info("Successfully updated company with id {}", id);

        return CompanyMapper.toCompanyDTO(savedUpdatedCompany);
    }

    //Fallback Methods

    public void deleteCompanyByIdRateLimitFallback(Long companyId, RequestNotPermitted e){

        logger.warn("Rate Limit Exceeded... while deleting company with id {}", companyId);

        throw new TooManyRequestsException(
                "Too many requests to delete Company. Please try again later.", e);
    }

    public CompanyDTO createCompanyRateLimitFallback(CreateCompanyRequest companyRequest, RequestNotPermitted e){

        logger.warn("Rate Limit Exceeded... while creating '{}' company", companyRequest.getName());

        throw new TooManyRequestsException(
                "Too many requests to create Company. Please try again later.", e);
    }

    public List<CompanyDTO> findAllCompaniesRateLimitFallback(RequestNotPermitted e){

        logger.warn("Rate Limit Exceeded... while finding all companies");

        throw new TooManyRequestsException(
                "Too many requests to find Companies. Please try again later.", e);
    }

    public CompanyDTO findCompanyByIdRateLimitFallback(Long id, RequestNotPermitted e){

        logger.warn("Rate Limit Exceeded... while finding company with id {}", id);

        throw new TooManyRequestsException(
                "Too many requests to find Company by ID. Please try again later.", e);
    }

    public CompanyDTO updateCompanyByIdRateLimitFallback(Long id, UpdateCompanyRequest updateCompanyRequest, RequestNotPermitted e){

        logger.warn("Rate Limit Exceeded... while updating company with id {}", id);

        throw new TooManyRequestsException(
                "Too many requests to update Company. Please try again later.", e);
    }
}
