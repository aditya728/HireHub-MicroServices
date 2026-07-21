package com.charlie.hirehub.companyservice.company;

import com.charlie.hirehub.companyservice.company.dto.request.CreateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.request.UpdateCompanyRequest;
import com.charlie.hirehub.companyservice.company.dto.response.CompanyDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController{

    CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CompanyDTO>> findAllCompanies(){
        List<CompanyDTO> companies = companyService.findAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CompanyDTO> findCompanyById(@PathVariable Long id){

        CompanyDTO company = companyService.findCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CreateCompanyRequest companyRequest){
        CompanyDTO companyDTO = companyService.createCompany(companyRequest);
        return new ResponseEntity<>(companyDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @companySecurity.isOwner(#id)")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable Long id){

        companyService.deleteCompanyById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @companySecurity.isOwner(#id)")
    public ResponseEntity<CompanyDTO> updateCompanyById(@PathVariable Long id, @RequestBody UpdateCompanyRequest updateCompanyRequest){

        CompanyDTO companyUpdated = companyService.updateCompanyById(id, updateCompanyRequest);
        return new ResponseEntity<>(companyUpdated, HttpStatus.OK);
    }
}