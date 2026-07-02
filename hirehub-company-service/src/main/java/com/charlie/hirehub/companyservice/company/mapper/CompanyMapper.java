package com.charlie.hirehub.companyservice.company.mapper;

import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.dto.request.CreateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.request.UpdateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;

public class CompanyMapper {

    public static CompanyDTO toCompanyDTO(Company company) {

        CompanyDTO dto = new CompanyDTO();

        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());

        return dto;
    }

    public static Company toCompany(CreateCompanyRequest companyRequest) {

        Company company = new Company();

        company.setName(companyRequest.getName());
        company.setDescription(companyRequest.getDescription());

        return company;
    }

    public static Company toCompany(UpdateCompanyRequest updateCompanyRequest) {

        Company company = new Company();

        company.setName(updateCompanyRequest.getName());
        company.setDescription(updateCompanyRequest.getDescription());

        return company;
    }
}
