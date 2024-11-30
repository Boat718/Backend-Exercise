package com.exercise.jobdataapi.repository;

import com.exercise.jobdataapi.entity.JobData;

import java.util.List;

public interface JobDataRepositoryCustom {
    List<JobData> findFilteredJobData(Long salaryGreaterThanOrEqual, Long salaryLessThanOrEqual, String jobTitle, String gender);
}
