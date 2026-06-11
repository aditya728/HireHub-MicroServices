package com.charlie.hirehub.companyservice.company;

import java.util.List;

public interface CompanyService {

    List<Company> findAllCompanies();

    Company findCompanyById(Long id);

    void createCompany(Company company);

    boolean deleteCompanyById(Long id);

    boolean updateCompanyById(Long id, Company updatedCompany);
}
