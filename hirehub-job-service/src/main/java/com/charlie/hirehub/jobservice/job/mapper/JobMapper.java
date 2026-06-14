package com.charlie.hirehub.jobservice.job.mapper;

import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.dto.JobDTO;
import com.charlie.hirehub.jobservice.job.external.Company;
import com.charlie.hirehub.jobservice.job.external.Review;

import java.util.List;

public class JobMapper {

    public static JobDTO mapToJobWithCompanyDTO(
            Job job, Company company, List<Review> reviews){

        JobDTO jobWithCompany = new JobDTO();

        jobWithCompany.setId(job.getId());
        jobWithCompany.setTitle(job.getTitle());
        jobWithCompany.setDescription(job.getDescription());
        jobWithCompany.setMinSalary(job.getMinSalary());
        jobWithCompany.setMaxSalary(job.getMaxSalary());
        jobWithCompany.setLocation(job.getLocation());

        jobWithCompany.setCompany(company);

        jobWithCompany.setReviews(reviews);

        return jobWithCompany;
    }
}
