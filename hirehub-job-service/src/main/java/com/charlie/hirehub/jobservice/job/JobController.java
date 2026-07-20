package com.charlie.hirehub.jobservice.job;

import com.charlie.hirehub.jobservice.job.dto.request.CreateJobRequest;
import com.charlie.hirehub.jobservice.job.dto.request.UpdateJobRequest;
import com.charlie.hirehub.jobservice.job.dto.response.JobCreatedResponse;
import com.charlie.hirehub.jobservice.job.dto.response.JobDetailsResponse;
import com.charlie.hirehub.jobservice.job.dto.response.UpdateJobResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController{

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<JobDetailsResponse>> findAllJobs(){
        return new ResponseEntity<>(jobService.findAllJobs(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<JobCreatedResponse> createJob(@Valid @RequestBody CreateJobRequest job){
        JobCreatedResponse jobResponse = jobService.createJob(job);
        return new ResponseEntity<>(jobResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JobDetailsResponse> getJobById(@PathVariable Long id){
        JobDetailsResponse jobWithCompany =  jobService.getJobById(id);
        return new ResponseEntity<>(jobWithCompany, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @jobSecurity.isOwner(#id)")
    public ResponseEntity<Void> deleteJobById(@PathVariable Long id){

        jobService.deleteJobById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @jobSecurity.isOwner(#id)")
    public ResponseEntity<UpdateJobResponse> updateJobById(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateJobRequest updatedJob){

        UpdateJobResponse job = jobService.updateJobById(id, updatedJob);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/exists")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> existsJobsByCompanyId(@PathVariable Long companyId){

        boolean jobExists = jobService.existsJobsByCompanyId(companyId);
        return new ResponseEntity<>(jobExists, HttpStatus.OK);
    }
}
