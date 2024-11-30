package com.exercise.jobdataapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "salary")
public class JobData {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIMESTAMP", length = 255)
    private String timestamp;

    @Column(name = "EMPLOYER", length = 255)
    private String employer;

    @Column(name = "LOCATION", length = 255)
    private String location;

    @Column(name = "JOB TITLE", length = 255)
    private String jobTitle;

    @Column(name = "YEARS AT EMPLOYER", length = 255)
    private double yearsAtEmployer;

    @Column(name = "YEARS OF EXPERIENCE", length = 255)
    private double yearsOfExperience;

    @Column(name = "SALARY", length = 255)
    private BigInteger salary;

    @Column(name = "SIGNING BONUS", length = 255)
    private int signingBonus;

    @Column(name = "ANNUAL BONUS", length = 255)
    private int annualBonus;

    @Column(name = "ANNUAL STOCK VALUE/BONUS", length = 255)
    private int annualStockValueBonus;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "ADDITIONAL COMMENTS", columnDefinition = "TEXT")
    private String additionalComments;

}
