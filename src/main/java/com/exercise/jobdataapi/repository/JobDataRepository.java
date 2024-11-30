package com.exercise.jobdataapi.repository;


import com.exercise.jobdataapi.entity.JobData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobDataRepository extends JpaRepository<JobData, Long>, JobDataRepositoryCustom {
    List<JobData> findBySalaryGreaterThanEqualAndJobTitleContainingIgnoreCaseAndGender(Long salaryGreaterThanOrEqual, String jobTitle, String gender);

    List<JobData> findBySalaryGreaterThanEqualAndJobTitleContainingIgnoreCase(Long salaryGreaterThanOrEqual, String jobTitle);

    List<JobData> findBySalaryGreaterThanEqualAndGender(Long salaryGreaterThanOrEqual, String gender);

    List<JobData> findByJobTitleContainingIgnoreCaseAndGender(String jobTitle, String gender);

    List<JobData> findBySalaryGreaterThanEqual(Long salaryGreaterThanOrEqual);

    List<JobData> findByGender(String gender);

    List<JobData> findByJobTitleContainingIgnoreCase(String jobTitle);
}
