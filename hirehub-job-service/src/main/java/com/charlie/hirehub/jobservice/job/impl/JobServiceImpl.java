package com.charlie.hirehub.jobservice.job.impl;


import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.JobRepository;
import com.charlie.hirehub.jobservice.job.JobService;
import com.charlie.hirehub.jobservice.job.dto.JobDTO;
import com.charlie.hirehub.jobservice.job.external.Company;
import com.charlie.hirehub.jobservice.job.external.Review;
import com.charlie.hirehub.jobservice.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    JobRepository jobRepo;

    @Autowired
    RestTemplate restTemplate;

    public JobServiceImpl(JobRepository jobRepo){
        this.jobRepo = jobRepo;
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
                company = restTemplate.getForObject("http://COMPANY-SERVICE/company/" + companyId, Company.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange("http://REVIEW-SERVICE/reviews?companyId=" + companyId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Review>>(){}
                );
                reviews = reviewResponse.getBody();
            }catch(Exception e) {
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
