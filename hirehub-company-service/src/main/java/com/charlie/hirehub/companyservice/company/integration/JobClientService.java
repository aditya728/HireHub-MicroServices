package com.charlie.hirehub.companyservice.company.integration;

import com.charlie.hirehub.companyservice.company.client.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobClientService {

    private final JobClient jobClient;

    private static final Logger logger =
            LoggerFactory.getLogger(JobClientService.class);

    public JobClientService(JobClient jobClient) {
        this.jobClient = jobClient;
    }

    public boolean jobWithCompanyIdExists(Long companyId){

        logger.info("Calling Job Service to check whether jobs exist for company {}.",
                companyId);

        return jobClient.jobWithCompanyIdExists(companyId);
    }
}
