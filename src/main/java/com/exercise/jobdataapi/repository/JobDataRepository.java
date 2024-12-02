package com.exercise.jobdataapi.repository;


import com.exercise.jobdataapi.entity.JobData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JobDataRepository extends JpaRepository<JobData, Long>, JobDataRepositoryCustom {
}
