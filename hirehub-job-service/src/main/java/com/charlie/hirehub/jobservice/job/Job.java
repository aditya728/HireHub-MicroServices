package com.charlie.hirehub.jobservice.job;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer minSalary;
    private Integer maxSalary;
    private String location;

    private long companyId;

    /*
    A noArg Constructor is needed as JPA needs to create instances of entity class while retrieval of data
     */
    public Job(){}

    public Job(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
