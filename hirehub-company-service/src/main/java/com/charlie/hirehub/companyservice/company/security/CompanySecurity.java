package com.charlie.hirehub.companyservice.company.security;

import com.charlie.hirehub.companyservice.company.Company;
import com.charlie.hirehub.companyservice.company.CompanyRepository;
import com.charlie.hirehub.companyservice.company.exception.CompanyNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CompanySecurity {

    private final CompanyRepository companyRepository;

    public CompanySecurity(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public boolean isOwner(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id " + companyId));

        return company.getCreatedBy().equals(getCurrentUserId());
    }

    public Long getCurrentUserId(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        AuthenticatedPrincipal principal =
                (AuthenticatedPrincipal) authentication.getPrincipal();

        return principal.getUserId();
    }
}
