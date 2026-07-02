package com.charlie.hirehub.reviewservice.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostReviewRequest {

    @NotBlank(message = "Review title is required")
    private String title;

    @NotBlank(message = "Review description is required")
    private String reviewDescription;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private double rating;

    @NotNull(message = "Company Id is required")
    private Long companyId;
}
