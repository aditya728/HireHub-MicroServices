package com.charlie.hirehub.jobservice.job;

import com.charlie.hirehub.jobservice.job.dto.JobDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController{

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> findAllJobs(){
        return new ResponseEntity<>(jobService.findAllJobs(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createJob(@Valid @RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity<>("Job added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id){
        JobDTO jobWithCompany =  jobService.getJobById(id);
        if(jobWithCompany != null){
            return new ResponseEntity<>(jobWithCompany, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id){

        jobService.deleteJobById(id);
        return new ResponseEntity<>("Job deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJobById(@PathVariable Long id,@RequestBody Job updatedJob){

        jobService.updateJobById(id, updatedJob);
        return new ResponseEntity<>("Job updated successfully", HttpStatus.OK);
    }
}
