package com.charlie.hirehub.jobservice.job;

import java.util.List;

public interface JobService {

    List<Job> findAllJobs();

    void createJob(Job job);

    Job getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJobById(Long id, Job updatedJob);
}
