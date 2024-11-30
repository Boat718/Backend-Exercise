package com.exercise.jobdataapi.repository.impl;

import com.exercise.jobdataapi.entity.JobData;
import com.exercise.jobdataapi.repository.JobDataRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class JobDataRepositoryCustomImpl implements JobDataRepositoryCustom {

    @Autowired
    private final EntityManager entityManager;

    public List<JobData> findFilteredJobData(Long salaryGreaterThanOrEqual, Long salaryLessThanOrEqual, String jobTitle, String gender) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobData> query = cb.createQuery(JobData.class);
        Root<JobData> jobDataRoot = query.from(JobData.class);

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(salaryGreaterThanOrEqual)) {
            predicates.add(cb.greaterThanOrEqualTo(jobDataRoot.get("salary"), salaryGreaterThanOrEqual));
        }

        if (Objects.nonNull(salaryLessThanOrEqual)) {
            predicates.add(cb.lessThanOrEqualTo(jobDataRoot.get("salary"), salaryLessThanOrEqual));
        }

        if (Objects.nonNull(jobTitle) && !jobTitle.isEmpty()) {
            predicates.add(cb.like(cb.lower(jobDataRoot.get("jobTitle")), "%" + jobTitle.toLowerCase() + "%"));
        }

        if (Objects.nonNull(gender) && !gender.isEmpty()) {
            predicates.add(cb.equal(jobDataRoot.get("gender"), gender));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
