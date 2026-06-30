package com.charlie.hirehub.companyservice.company.integration;

import com.charlie.hirehub.companyservice.company.client.ReviewClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReviewClientService {

    private final ReviewClient reviewClient;

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewClientService.class);

    public ReviewClientService(ReviewClient reviewClient) {
        this.reviewClient = reviewClient;
    }

    public boolean reviewsExistsByCompanyId(Long companyId){

        logger.info("Calling Review Service to check whether review(s) exist for company {}.",
                companyId);

        return reviewClient.reviewsExistsByCompanyId(companyId);
    }
}
