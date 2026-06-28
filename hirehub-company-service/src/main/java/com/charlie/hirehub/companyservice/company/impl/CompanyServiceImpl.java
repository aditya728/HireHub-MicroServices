package com.charlie.hirehub.companyservice.company.impl;


import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.CompanyRepository;
import com.charlie.hirehub.companyservice.company.CompanyService;
import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;
import com.charlie.hirehub.companyservice.company.exception.CompanyNotFoundException;
import com.charlie.hirehub.companyservice.company.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepo;

    private static final Logger logger =
            LoggerFactory.getLogger(CompanyServiceImpl.class);

    public CompanyServiceImpl(CompanyRepository companyRepo){
        this.companyRepo = companyRepo;
    }

    @Override
    public List<Company> findAllCompanies() {

        logger.debug("Fetching all Companies");

        List<Company> companies = companyRepo.findAll();

        logger.info("Successfully fetched {} companies", companies.size());

        return companies;
    }

    @Override
    public Company findCompanyById(Long id) {

        logger.debug("Fetching Company with id: {}", id);

        Company company = companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company with id: " + id + " does not exist"));

        logger.debug("Successfully fetched Company with id: {}", id);
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
    public boolean deleteCompanyById(Long id) {
        if(companyRepo.existsById(id)){
            companyRepo.deleteById(id);
            return true;
        }
        return false;
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
