package com.charlie.hirehub.companyservice.company;

import com.charlie.hirehub.companyservice.company.dto.request.CreateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.request.UpdateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;

import java.util.List;

public interface CompanyService {

    List<CompanyDTO> findAllCompanies();

    CompanyDTO findCompanyById(Long id);

    CompanyDTO createCompany(CreateCompanyRequest companyRequest);

    void deleteCompanyById(Long id);

    CompanyDTO updateCompanyById(Long id, UpdateCompanyRequest updateCompanyRequest);
}
