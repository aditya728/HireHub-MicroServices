package com.charlie.hirehub.jobservice.job;

import com.charlie.hirehub.jobservice.job.dto.JobWithCompanyDTO;

import java.util.List;

public interface JobService {

    List<JobWithCompanyDTO> findAllJobs();

    void createJob(Job job);

    Job getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJobById(Long id, Job updatedJob);
}
