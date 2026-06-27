package com.charlie.hirehub.jobservice.job.impl;


import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.JobRepository;
import com.charlie.hirehub.jobservice.job.JobService;
import com.charlie.hirehub.jobservice.job.dto.JobDTO;
import com.charlie.hirehub.jobservice.job.exception.JobNotFoundException;
import com.charlie.hirehub.jobservice.job.exception.TooManyRequestsException;
import com.charlie.hirehub.jobservice.job.external.Company;
import com.charlie.hirehub.jobservice.job.external.Review;
import com.charlie.hirehub.jobservice.job.integration.CompanyClientService;
import com.charlie.hirehub.jobservice.job.integration.ReviewClientService;
import com.charlie.hirehub.jobservice.job.mapper.JobMapper;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepo;

    private final CompanyClientService companyClientService;

    private final ReviewClientService reviewClientService;

    public JobServiceImpl(JobRepository jobRepo, CompanyClientService companyClientService, ReviewClientService reviewClientService){
        this.jobRepo = jobRepo;
        this.companyClientService = companyClientService;
        this.reviewClientService = reviewClientService;
    }

    @Override
    @RateLimiter(name = "readJobRateLimiter", fallbackMethod = "getJobRateLimiterFallback")
    public List<JobDTO> findAllJobs() {
        List<Job> jobs= jobRepo.findAll();

        List<JobDTO> jobsWithCompanyDetails = new ArrayList<>();
        for(Job job : jobs){
            JobDTO jobWithCompany = convertToJobWithCompanyDto(job);
            jobsWithCompanyDetails.add(jobWithCompany);
        }
        return jobsWithCompanyDetails;
    }

    private JobDTO convertToJobWithCompanyDto(Job job){

        Company company = null;
        List<Review> reviews = Collections.emptyList();

        Long companyId = job.getCompanyId();
        if(companyId != null) {
                company = companyClientService.getCompany(companyId);

                reviews = reviewClientService.getReviews(companyId);
        }
        return JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
    }

    @Override
    @RateLimiter(name = "writeJobRateLimiter", fallbackMethod = "createJobRateLimiterFallback")
    public void createJob(Job job) {
        Long companyId = job.getCompanyId();
        companyClientService.validateCompany(companyId);
        jobRepo.save(job);
    }

    @Override
    @RateLimiter(name = "readJobRateLimiter", fallbackMethod = "getJobByIdRateLimiterFallback")
    public JobDTO getJobById(Long id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job with id: " + id + " does not exist"));

        return convertToJobWithCompanyDto(job);
    }

    @Override
    @RateLimiter(name = "writeJobRateLimiter", fallbackMethod = "deleteJobByIdRateLimiterFallback")
    public void deleteJobById(Long id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job with id: " + id + " does not exist"));
        jobRepo.delete(job);
    }

    @Override
    @RateLimiter(name = "writeJobRateLimiter", fallbackMethod = "updateJobByIdRateLimiterFallback")
    public void updateJobById(Long id, Job updatedJob) {

        Job oldJob = jobRepo.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job with id: " + id + " does not exist"));

        oldJob.setTitle(updatedJob.getTitle());
        oldJob.setDescription((updatedJob.getDescription()));
        oldJob.setLocation(updatedJob.getLocation());
        oldJob.setMinSalary(updatedJob.getMinSalary());
        oldJob.setMaxSalary(updatedJob.getMaxSalary());

        jobRepo.save(oldJob);
    }

    //Fallback Methods:
    public void createJobRateLimiterFallback(Job job, RequestNotPermitted e) {
        throw new TooManyRequestsException(
                "Too many requests to create job. Please try again later.", e);
    }

    public List<JobDTO> getJobRateLimiterFallback(RequestNotPermitted e){
        System.out.println("RATE LIMIT EXCEEDED");
        throw new TooManyRequestsException(
                "Too many requests to fetch jobs. Please try again later.", e);
    }

    public JobDTO getJobByIdRateLimiterFallback(Long id, RequestNotPermitted e) {
        throw new TooManyRequestsException(
                "Too many requests to fetch job. Please try again later.", e);
    }

    public void deleteJobByIdRateLimiterFallback(Long id, RequestNotPermitted e) {
        throw new TooManyRequestsException(
                "Too many requests to delete job. Please try again later.", e);
    }

    public void updateJobByIdRateLimiterFallback(Long id, RequestNotPermitted e) {
        throw new TooManyRequestsException(
                "Too many requests to update job. Please try again later.", e);
    }

}
