package com.charlie.hirehub.jobservice.job.impl;


import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.JobRepository;
import com.charlie.hirehub.jobservice.job.JobService;
import com.charlie.hirehub.jobservice.job.dto.JobWithCompanyDTO;
import com.charlie.hirehub.jobservice.job.external.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
    public List<JobWithCompanyDTO> findAllJobs() {
        List<Job> jobs= jobRepo.findAll();

        List<JobWithCompanyDTO> jobsWithCompanyDetails = new ArrayList<>();
        for(Job job : jobs){
            JobWithCompanyDTO jobWithCompany = convertToJobWithCompanyDto(job);
            jobsWithCompanyDetails.add(jobWithCompany);
        }

        return jobsWithCompanyDetails;
    }

    private JobWithCompanyDTO convertToJobWithCompanyDto(Job job){

        JobWithCompanyDTO jobWithCompany = new JobWithCompanyDTO();
        jobWithCompany.setJob(job);

        Long companyId = job.getCompanyId();
        if(companyId != null)
            try{
                Company company = restTemplate.getForObject("http://COMPANY-SERVICE/company/" + companyId, Company.class);
                jobWithCompany.setCompany(company);
            }catch(Exception e){
                jobWithCompany.setCompany(null);
            }
        return jobWithCompany;
    }

    @Override
    public void createJob(Job job) {
        jobRepo.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepo.findById(id).orElse(null);
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
