package com.charlie.hirehub.jobservice.job;

import com.charlie.hirehub.jobservice.job.dto.request.CreateJobRequest;
import com.charlie.hirehub.jobservice.job.dto.request.UpdateJobRequest;
import com.charlie.hirehub.jobservice.job.dto.response.JobCreatedResponse;
import com.charlie.hirehub.jobservice.job.dto.response.JobDetailsResponse;

import java.util.List;

public interface JobService {

    List<JobDetailsResponse> findAllJobs();

    JobCreatedResponse createJob(CreateJobRequest job);

    JobDetailsResponse getJobById(Long id);

    void deleteJobById(Long id);

    Job updateJobById(Long id, UpdateJobRequest updatedJob);

    boolean existsJobsByCompanyId(Long companyId);
}
