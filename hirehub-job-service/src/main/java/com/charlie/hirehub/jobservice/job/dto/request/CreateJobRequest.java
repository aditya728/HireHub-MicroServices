package com.charlie.hirehub.jobservice.job.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateJobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Minimum salary is required")
    private Integer minSalary;

    @NotNull(message = "Maximum salary is required")
    private Integer maxSalary;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Company Id is required")
    private Long companyId;
}
