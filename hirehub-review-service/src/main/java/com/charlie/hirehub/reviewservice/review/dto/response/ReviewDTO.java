package com.charlie.hirehub.reviewservice.review.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {

    private long id;
    private String title;
    private String reviewDescription;
    private double rating;

    private Long companyId;
}
