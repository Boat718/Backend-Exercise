package com.exercise.jobdataapi.service;

import com.exercise.jobdataapi.dto.ResponseDTO;
import org.springframework.web.bind.annotation.RequestParam;

public interface JobDataService {

    ResponseDTO getFilteredJobData(Long salaryGreaterThanOrEqual, String jobTitle, String gender);

    ResponseDTO getFilteredJobDataBySparseFields(String fields);
}
