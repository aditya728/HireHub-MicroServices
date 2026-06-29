package com.charlie.hirehub.companyservice.company.impl;


import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.CompanyRepository;
import com.charlie.hirehub.companyservice.company.CompanyService;
import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;
import com.charlie.hirehub.companyservice.company.exception.CompanyHasDependencyException;
import com.charlie.hirehub.companyservice.company.exception.CompanyNotFoundException;
import com.charlie.hirehub.companyservice.company.integration.JobClientService;
import com.charlie.hirehub.companyservice.company.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepo;
    JobClientService jobClientService;


    private static final Logger logger =
            LoggerFactory.getLogger(CompanyServiceImpl.class);

    public CompanyServiceImpl(CompanyRepository companyRepo, JobClientService jobClientService){
        this.companyRepo = companyRepo;
        this.jobClientService = jobClientService;
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
    public void deleteCompanyById(Long id) {

        logger.info("Deleting company with id {}", id);

        Company company = companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company with id " + id + " not found"));

        //Search if company has jobs and reviews on for itself
        if(jobClientService.jobWithCompanyIdExists(id)){
            logger.warn("Can't delete company with id {} because it has Jobs", id);
            throw new CompanyHasDependencyException("Cannot delete company because Jobs exist for this company.");
        }

        companyRepo.delete(company);

        logger.info("Successfully deleted company with id {}", id);
    }

    @Override
    public boolean updateCompanyById(Long id, Company updatedCompany) {
        Company currentCompany = companyRepo.findById(id).orElse(null);

        if(currentCompany != null){
            currentCompany.setName(updatedCompany.getName());
            currentCompany.setDescription(updatedCompany.getDescription());

            companyRepo.save(currentCompany);
            return true;
        }
        return false;
    }
}
