package com.charlie.hirehub.jobservice.job.impl;


import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.JobRepository;
import com.charlie.hirehub.jobservice.job.JobService;
import com.charlie.hirehub.jobservice.job.clients.ReviewClient;
import com.charlie.hirehub.jobservice.job.dto.JobDTO;
import com.charlie.hirehub.jobservice.job.external.Company;
import com.charlie.hirehub.jobservice.job.external.Review;
import com.charlie.hirehub.jobservice.job.integration.CompanyClientService;
import com.charlie.hirehub.jobservice.job.mapper.JobMapper;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepo;

    private final CompanyClientService companyClientService;

    private final ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepo, CompanyClientService companyClientService, ReviewClient reviewClient){
        this.jobRepo = jobRepo;
        this.companyClientService = companyClientService;
        this.reviewClient = reviewClient;
    }

    @Override
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
            try {
                company = companyClientService.getCompany(companyId);
            } catch (FeignException.NotFound e) {
                e.printStackTrace();
            }
            try{
                reviews = reviewClient.getReviews(companyId);
            }catch(FeignException.NotFound e) {
                e.printStackTrace();
            }
        }
        return JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
    }

    @Override
    public void createJob(Job job) {
        jobRepo.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepo.findById(id).orElse(null);
        if(job == null){
            return null;
        }
        return convertToJobWithCompanyDto(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        if(jobRepo.existsById(id)){
            jobRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateJobById(Long id, Job updatedJob) {

        Optional<Job> optionalJob = jobRepo.findById(id);

        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();

            job.setTitle(updatedJob.getTitle());
            job.setDescription((updatedJob.getDescription()));
            job.setLocation(updatedJob.getLocation());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());

            jobRepo.save(job);

            return true;
        }
        return false;
    }
}
