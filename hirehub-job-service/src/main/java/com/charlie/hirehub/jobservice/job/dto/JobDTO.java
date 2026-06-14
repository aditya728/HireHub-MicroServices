package com.charlie.hirehub.jobservice.job.dto;

import com.charlie.hirehub.jobservice.job.external.Company;
import com.charlie.hirehub.jobservice.job.external.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class JobDTO {

    private Long id;
    private String title;
    private String description;
    private Integer minSalary;
    private Integer maxSalary;
    private String location;

    private Company company;

    private List<Review> reviews;
}
