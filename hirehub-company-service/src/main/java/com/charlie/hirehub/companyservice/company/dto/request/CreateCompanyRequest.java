package com.charlie.hirehub.companyservice.company.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    @NotBlank(message = "Company description is required")
    private String description;
}
