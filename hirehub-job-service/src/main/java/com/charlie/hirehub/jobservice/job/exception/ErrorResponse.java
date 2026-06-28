package com.charlie.hirehub.jobservice.job.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

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

        // Only populated for validation errors
        private Map<String, String> validationErrors;

        public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
                this.timestamp = timestamp;
                this.status = status;
                this.error = error;
                this.message = message;
                this.path = path;
        }
}