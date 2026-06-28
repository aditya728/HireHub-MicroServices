package com.charlie.hirehub.companyservice.company.mapper;

import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;

public class CompanyMapper {

    public static CompanyDTO toCompanyDTO(Company company) {

        CompanyDTO dto = new CompanyDTO();

        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());

        return dto;
    }
}
