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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.exercise.jobdataapi.util.HelperClass.*;

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
    public ResponseDTO processJobDataRequest(Long salaryGreaterThanOrEqual, Long salaryLessThanOrEqual, String jobTitle, String gender,
                                             String fields, String sort, String sortType) {

        if (isSparseFieldRequest(fields, salaryGreaterThanOrEqual,salaryLessThanOrEqual, jobTitle, gender, sort)) {
            return getFilteredJobDataBySparseFields(fields);
        }

        if (isSortRequest(sort, salaryGreaterThanOrEqual,salaryLessThanOrEqual, jobTitle, gender, fields)) {
            return getSortedJobDataByField(sort, sortType);
        }

        if (isFilterRequest(salaryGreaterThanOrEqual,salaryLessThanOrEqual, jobTitle, gender, fields, sort)) {
            return getFilteredJobData(salaryGreaterThanOrEqual,salaryLessThanOrEqual, jobTitle, gender);
        }

        throw new IllegalArgumentException("Invalid request params");
    }

    @Override
    public ResponseDTO getFilteredJobData(Long salaryGreaterThanOrEqual, Long salaryLessThanOrEqual, String jobTitle, String gender) {
        try{
            List<JobData> jobData = jobDataRepository.findFilteredJobData(salaryGreaterThanOrEqual, salaryLessThanOrEqual, jobTitle, gender);

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
                    .map(field -> {
                        if ("annual_stock_value_bonus".equals(field)) {
                            return "\"ANNUAL STOCK VALUE/BONUS\""; // Special case
                        } else {
                            return "\"" + field.replace("_", " ").toUpperCase() + "\""; // General case
                        }
                    })
                    .collect(Collectors.joining(","));

            List<?> resultList = findByDynamicFields(columnName);

            List<Map<String, Object>> filteredData = new ArrayList<>();

            if (resultList.get(0) instanceof Object[]) {
                for (Object[] row : (List<Object[]>) resultList) {
                    Map<String, Object> rowMap = new HashMap<>();
                    for (int i = 0; i < row.length; i++) {
                        rowMap.put(jsonProperty.get(i), row[i] != null ? row[i] : "");
                    }
                    filteredData.add(rowMap);
                }
            } else {
                for (Object row : resultList) {
                    Map<String, Object> rowMap = new HashMap<>();
                    rowMap.put(jsonProperty.get(0), row != null ? row : "");
                    filteredData.add(rowMap);
                }
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

    @Override
    public ResponseDTO getSortedJobDataByField(String sortBy, String sortType) {

        try{
            validateFields(sortBy);
            String columnName = convertToCamelCase(sortBy);

            if (!sortType.equalsIgnoreCase("asc") && !sortType.equalsIgnoreCase("desc")) {
                throw new IllegalArgumentException("Invalid sortType. Allowed values are 'asc' or 'desc'");
            }

            Sort.Direction direction = sortType.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

            List<JobData> jobData = jobDataRepository.findAll(Sort.by(direction, columnName));

            List<JobDataDTO> dataDTOS = List.of(mapper.convertValue(jobData, JobDataDTO[].class));

            return ResponseDTO.builder()
                    .status(HttpStatus.OK)
                    .data(dataDTOS)
                    .build();

        } catch (IllegalArgumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error fetching sorted job data", e);
        }
    }

    private List<?> findByDynamicFields(String fields) {
        String sql = "SELECT " + fields + " FROM salary";
        Query query = entityManager.createNativeQuery(sql);
        List<?> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return new ArrayList<>();
        }

        resultList = resultList.stream().map(row -> {
            if (row instanceof Object[]) {

                Object[] rowArray = (Object[]) row;
                for (int i = 0; i < rowArray.length; i++) {
                    if (rowArray[i] instanceof Clob) {
                        try {
                            Clob clob = (Clob) rowArray[i];
                            rowArray[i] = clob.getSubString(1, (int) clob.length());
                        } catch (SQLException e) {
                            throw new RuntimeException("Error converting CLOB to String", e);
                        }
                    }
                }
                return rowArray;
            } else if (row instanceof Clob) {

                Clob clob = (Clob) row;
                try {
                    return clob.getSubString(1, (int) clob.length());
                } catch (SQLException e) {
                    throw new RuntimeException("Error converting CLOB to String", e);
                }
            }
            return row;
        }).collect(Collectors.toList());

        return  resultList;
    }

}
