package com.exercise.jobdataapi.service;

import com.exercise.jobdataapi.dto.ResponseDTO;

public interface JobDataService {

    ResponseDTO processJobDataRequest(Long salaryGreaterThanOrEqual,Long salaryLessThanOrEqual,String jobTitle, String gender,
                                      String fields, String sort, String sortType);

    ResponseDTO getFilteredJobData(Long salaryGreaterThanOrEqual, Long salaryLessThanOrEqual, String jobTitle, String gender);

    ResponseDTO getFilteredJobDataBySparseFields(String fields);

    ResponseDTO getSortedJobDataByField(String sortBy, String sortType);
}
