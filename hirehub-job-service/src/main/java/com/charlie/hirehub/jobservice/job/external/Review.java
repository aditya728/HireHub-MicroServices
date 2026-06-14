package com.charlie.hirehub.jobservice.job.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private long id;
    private String title;
    private String reviewDescription;
    private double rating;
}
