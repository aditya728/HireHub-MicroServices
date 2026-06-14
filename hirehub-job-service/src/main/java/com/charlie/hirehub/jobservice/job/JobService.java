package com.charlie.hirehub.jobservice.job;

import com.charlie.hirehub.jobservice.job.dto.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> findAllJobs();

    void createJob(Job job);

    JobDTO getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJobById(Long id, Job updatedJob);
}
