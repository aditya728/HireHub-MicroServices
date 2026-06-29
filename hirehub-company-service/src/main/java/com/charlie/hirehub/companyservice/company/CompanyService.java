package com.charlie.hirehub.companyservice.company;

import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;

import java.util.List;

public interface CompanyService {

    List<Company> findAllCompanies();

    Company findCompanyById(Long id);

    CompanyDTO createCompany(Company company);

    void deleteCompanyById(Long id);

    boolean updateCompanyById(Long id, Company updatedCompany);
}
