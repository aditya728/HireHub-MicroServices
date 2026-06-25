package com.charlie.hirehub.jobservice.job.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse{
        LocalDateTime timestamp;
        int status;
        String error;
        String message;
        String path;
}