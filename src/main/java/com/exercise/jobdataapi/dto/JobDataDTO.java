package com.exercise.jobdataapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Builder
public class JobDataDTO {

    private String timestamp;
    private String employer;
    private String location;
    private String jobTitle;
    private Double yearsAtEmployer;
    private Double yearsOfExperience;
    private BigInteger salary;
    private Integer signingBonus;
    private Integer annualBonus;
    private Integer annualStockValueBonus;
    private String gender;
    private String additionalComments;
}
