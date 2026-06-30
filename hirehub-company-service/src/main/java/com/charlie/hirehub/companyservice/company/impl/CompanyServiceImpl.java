package com.charlie.hirehub.companyservice.company.impl;


import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.CompanyRepository;
import com.charlie.hirehub.companyservice.company.CompanyService;
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

    CompanyRepository companyRepo;
    JobClientService jobClientService;
    ReviewClientService reviewClientService;

    private static final Logger logger =
            LoggerFactory.getLogger(CompanyServiceImpl.class);

    public CompanyServiceImpl(CompanyRepository companyRepo, JobClientService jobClientService, ReviewClientService reviewClientService){
        this.companyRepo = companyRepo;
        this.jobClientService = jobClientService;
        this.reviewClientService = reviewClientService;
    }

    @Override
    public List<Company> findAllCompanies() {

        logger.info("Fetching all Companies");

        List<Company> companies = companyRepo.findAll();

        logger.info("Successfully fetched {} companies", companies.size());

        return companies;
    }

    @Override
    public Company findCompanyById(Long id) {

        logger.info("Fetching Company with id: {}", id);

        Company company = companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company with id: " + id + " does not exist"));

        logger.info("Successfully fetched Company with id: {}", id);
        return company;
    }

    @Override
    public CompanyDTO createCompany(Company company) {

        logger.info("Creating a Company with name: {}", company.getName());

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
    public boolean updateCompanyById(Long id, Company updatedCompany) {

        logger.info("Updating company with id {}", id);

        Company currentCompany = companyRepo.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + id +" not found"));

        if(currentCompany != null){
            currentCompany.setName(updatedCompany.getName());
            currentCompany.setDescription(updatedCompany.getDescription());

            companyRepo.save(currentCompany);

            logger.info("Successfully updated company with id {}", id);

            return true;
        }

        logger.warn("Failed to update company with id {}", id);
        return false;
    }

    public void deleteCompanyByIdRateLimitFallback(Long companyId, RequestNotPermitted e){

        logger.warn("Rate Limit Exceeded while deleting Company");

        throw new TooManyRequestsException(
                "Too many requests to delete Company. Please try again later.", e);
    }
}
