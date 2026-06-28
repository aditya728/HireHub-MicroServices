package com.charlie.hirehub.jobservice.job.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JobCreatedResponse {

    private Long jobId;
    private String title;
    private String description;
    private String message;
}
