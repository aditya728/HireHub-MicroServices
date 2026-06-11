package com.charlie.hirehub.companyservice.company.impl;


import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.CompanyRepository;
import com.charlie.hirehub.companyservice.company.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepo;

    public CompanyServiceImpl(CompanyRepository companyRepo){
        this.companyRepo = companyRepo;
    }

    @Override
    public List<Company> findAllCompanies() {
        return companyRepo.findAll();
    }

    @Override
    public Company findCompanyById(Long id) {
        return companyRepo.findById(id).orElse(null);
    }

    @Override
    public void createCompany(Company company) {
        companyRepo.save(company);
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
//            currentCompany.setJobs(updatedCompany.getJobs());

            companyRepo.save(currentCompany);
            return true;
        }
        return false;
    }
}
