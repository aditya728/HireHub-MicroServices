package com.charlie.hirehub.jobservice.job.security;

import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.JobRepository;
import com.charlie.hirehub.jobservice.job.exception.JobNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JobSecurity {

    private final JobRepository jobRepository;

    public JobSecurity(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public boolean isOwner(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id " + jobId));

        return job.getCreatedBy().equals(getCurrentUserId());
    }

    public Long getCurrentUserId(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        AuthenticatedPrincipal principal =
                (AuthenticatedPrincipal) authentication.getPrincipal();

        return principal.getUserId();
    }
}
