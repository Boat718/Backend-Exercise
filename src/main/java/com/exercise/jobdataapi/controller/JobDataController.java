package com.exercise.jobdataapi.controller;


import com.exercise.jobdataapi.dto.JobDataDTO;
import com.exercise.jobdataapi.dto.ResponseDTO;
import com.exercise.jobdataapi.entity.JobData;
import com.exercise.jobdataapi.repository.JobDataRepository;
import com.exercise.jobdataapi.service.JobDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class JobDataController {

    @Autowired
    private final JobDataService jobDataService;

    //Filter by one or more fields/attributes (e.g. /job_data?salary[gte]=120000) (Show only filtered row. Expected filter able column: job title, salary, gender )
    //Filter by a sparse fields/attributes (e.g. /job_data?fields=job_title,gender,salary) (Show only filtered column)
    //Sort by one or more fields/attributes (e.g. /job_data?sort=job_title&sort_type=DESC)
    @GetMapping("/job_data")
    public ResponseEntity<ResponseDTO> getFilteredJobDataByFields(
            @RequestParam(name = "salary[gte]", required = false) Long salaryGreaterThanOrEqual,
            @RequestParam(name = "job_title", required = false) String jobTitle,
            @RequestParam(name = "gender", required = false) String gender,
            @RequestParam(name = "fields",  required = false) String fields,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "sort_type", required = false, defaultValue = "ASC") String sortType
    ) {
        ResponseDTO response = null;
        if(Objects.nonNull(fields)) {
            response = jobDataService.getFilteredJobDataBySparseFields(fields);
        }  else {
            response = jobDataService.getFilteredJobData(salaryGreaterThanOrEqual, jobTitle, gender);
        }

       return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
