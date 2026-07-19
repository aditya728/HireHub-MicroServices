package com.charlie.hirehub.jobservice.job.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor jwtPropagationInterceptor() {

        return requestTemplate -> {

            //Get current HTTP request that's being processed.
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                return;
            }

            // Extract the request
            HttpServletRequest request = attributes.getRequest();

            String authorizationHeader =
                    request.getHeader(HttpHeaders.AUTHORIZATION);

            //if there is no authHeader, then add one
            if (authorizationHeader != null) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }
        };
    }
}
