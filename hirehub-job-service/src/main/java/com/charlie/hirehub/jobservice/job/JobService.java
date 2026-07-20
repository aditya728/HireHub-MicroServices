package com.charlie.hirehub.jobservice.job;

import com.charlie.hirehub.jobservice.job.dto.request.CreateJobRequest;
import com.charlie.hirehub.jobservice.job.dto.request.UpdateJobRequest;
import com.charlie.hirehub.jobservice.job.dto.response.JobCreatedResponse;
import com.charlie.hirehub.jobservice.job.dto.response.JobDetailsResponse;
import com.charlie.hirehub.jobservice.job.dto.response.UpdateJobResponse;

import java.util.List;

public interface JobService {

    List<JobDetailsResponse> findAllJobs();

    JobCreatedResponse createJob(CreateJobRequest job);

    JobDetailsResponse getJobById(Long id);

    void deleteJobById(Long id);

    UpdateJobResponse updateJobById(Long id, UpdateJobRequest updatedJob);

    boolean existsJobsByCompanyId(Long companyId);
}
