package com.charlie.hirehub.jobservice.job.mapper;

import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.dto.request.CreateJobRequest;
import com.charlie.hirehub.jobservice.job.dto.response.JobCreatedResponse;
import com.charlie.hirehub.jobservice.job.dto.response.JobDetailsResponse;
import com.charlie.hirehub.jobservice.job.dto.response.UpdateJobResponse;
import com.charlie.hirehub.jobservice.job.external.Company;
import com.charlie.hirehub.jobservice.job.external.Review;

import java.util.List;

public class JobMapper {

    public static JobDetailsResponse mapToJobWithCompanyDTO(
            Job job, Company company, List<Review> reviews){

        JobDetailsResponse jobWithCompany = new JobDetailsResponse();

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

    public static Job mapToJob(CreateJobRequest jobRequest) {

        Job job = new Job();

        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setMinSalary(jobRequest.getMinSalary());
        job.setMaxSalary(jobRequest.getMaxSalary());
        job.setLocation(jobRequest.getLocation());
        job.setCompanyId(jobRequest.getCompanyId());

        return job;
    }

    public static JobCreatedResponse toJobCreatedResponse(Job job) {

        JobCreatedResponse response = new JobCreatedResponse();

        response.setJobId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());

        return response;
    }

    public static UpdateJobResponse toUpdateJobResponse(Job job) {

        UpdateJobResponse response = new UpdateJobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setMinSalary(job.getMinSalary());
        response.setMaxSalary(job.getMaxSalary());
        response.setLocation(job.getLocation());
        response.setCompanyId(job.getCompanyId());

        return response;
    }
}
