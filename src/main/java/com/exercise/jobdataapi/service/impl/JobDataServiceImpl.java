package com.exercise.jobdataapi.service.impl;

import com.exercise.jobdataapi.dto.JobDataDTO;
import com.exercise.jobdataapi.dto.ResponseDTO;
import com.exercise.jobdataapi.entity.JobData;
import com.exercise.jobdataapi.repository.JobDataRepository;
import com.exercise.jobdataapi.service.JobDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobDataServiceImpl implements JobDataService {

    @Autowired
    private final JobDataRepository jobDataRepository;

    @Autowired
    private final ObjectMapper mapper;

    @Autowired
    private final EntityManager entityManager;

    @Override
    public ResponseDTO getFilteredJobData(Long salaryGreaterThanOrEqual, String jobTitle, String gender) {

        try{
            List<JobData> jobData;

            if (Objects.nonNull(salaryGreaterThanOrEqual) && Objects.nonNull(jobTitle) && Objects.nonNull(gender)) {
                jobData = jobDataRepository.findBySalaryGreaterThanEqualAndJobTitleContainingIgnoreCaseAndGender(salaryGreaterThanOrEqual, jobTitle, gender);
            }

            else if (Objects.nonNull(salaryGreaterThanOrEqual) && Objects.nonNull(jobTitle)) {
                jobData = jobDataRepository.findBySalaryGreaterThanEqualAndJobTitleContainingIgnoreCase(salaryGreaterThanOrEqual, jobTitle);
            }

            else if (Objects.nonNull(salaryGreaterThanOrEqual) && Objects.nonNull(gender)) {
                jobData = jobDataRepository.findBySalaryGreaterThanEqualAndGender(salaryGreaterThanOrEqual, gender);
            }

            else if (Objects.nonNull(jobTitle) && Objects.nonNull(gender)) {
                jobData = jobDataRepository.findByJobTitleContainingIgnoreCaseAndGender(jobTitle, gender);
            }

            else if (Objects.nonNull(salaryGreaterThanOrEqual)) {
                jobData = jobDataRepository.findBySalaryGreaterThanEqual(salaryGreaterThanOrEqual);
            }

            else if (Objects.nonNull(jobTitle)) {
                jobData = jobDataRepository.findByJobTitleContainingIgnoreCase(jobTitle);
            }

            else if (Objects.nonNull(gender)) {
                jobData = jobDataRepository.findByGender(gender);
            }

            else {
                jobData = jobDataRepository.findAll();
            }

            List<JobDataDTO> dataDTOS = jobData.stream()
                    .map(job -> JobDataDTO.builder().salary(job.getSalary())
                            .jobTitle(job.getJobTitle()).gender(job.getGender()).build())
                    .toList();

            return ResponseDTO.builder()
                    .status(HttpStatus.OK)
                    .data(dataDTOS)
                    .build();


        } catch (Exception e) {
            throw new RuntimeException("Error fetching job data", e);
        }
    }

    @Override
    public ResponseDTO getFilteredJobDataBySparseFields(String fields) {
        try {
            validateFields(fields);
            List<String> jsonProperty = convertSnakeToCamelCase(fields);

            String columnName = Arrays.stream(fields.split(","))
                    .map(field -> "\"" + field.replace("_", " ").toUpperCase() + "\"")
                    .collect(Collectors.joining(","));

            List<Object[]> resultList = findByDynamicFields(columnName);

            List<Map<String, Object>> filteredData = new ArrayList<>();

            for (Object[] row : resultList) {
                Map<String, Object> rowMap = new HashMap<>();

                for (int i = 0; i < row.length; i++) {
                    rowMap.put(jsonProperty.get(i), row[i] != null ? row[i] : "");  // Handling null values
                }

                filteredData.add(rowMap);
            }

            List<JobDataDTO> dataDTOS = filteredData.stream()
                    .map(row -> {
                        try {
                            return mapper.convertValue(row, JobDataDTO.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Error converting map to DTO", e);
                        }
                    })
                    .collect(Collectors.toList());

            return ResponseDTO.builder()
                    .status(HttpStatus.OK)
                    .data(dataDTOS)
                    .build();

        }
        catch (IllegalArgumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error fetching filtered job data", e);
        }
    }

    private List<Object[]> findByDynamicFields(String fields) {
        String sql = "SELECT " + fields + " FROM salary";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    private static List<String> convertSnakeToCamelCase(String fields) {
        String[] snakeCaseArray = fields.split(",");
        List<String> camelCaseList = new ArrayList<>();

        for (String snakeCase : snakeCaseArray) {
            camelCaseList.add(convertToCamelCase(snakeCase));
        }

        return camelCaseList;
    }

    private static String convertToCamelCase(String snakeCase) {
        String[] words = snakeCase.split("_");
        StringBuilder camelCaseString = new StringBuilder(words[0].toLowerCase());

        for (int i = 1; i < words.length; i++) {
            camelCaseString.append(words[i].substring(0, 1).toUpperCase());
            camelCaseString.append(words[i].substring(1).toLowerCase());
        }

        return camelCaseString.toString();
    }

    private static void validateFields(String fields) {
        Set<String> allowedFields = new HashSet<>(Arrays.asList(
                "employer", "location", "job_title", "years_at_employer",
                "years_of_experience", "salary", "signing_bonus", "annual_bonus",
                "annual_stock_value_bonus", "gender", "additional_comments"
        ));

        String[] inputFields = fields.split(",");

        for (String field : inputFields) {
            if (!allowedFields.contains(field)) {
                throw new IllegalArgumentException("Invalid field: " + field);
            }
        }
    }

}
