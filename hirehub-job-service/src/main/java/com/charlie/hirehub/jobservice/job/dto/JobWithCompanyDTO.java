package com.charlie.hirehub.jobservice.job.dto;

import com.charlie.hirehub.jobservice.job.Job;
import com.charlie.hirehub.jobservice.job.external.Company;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobWithCompanyDTO {

    private Job job;
    private Company company;
}
