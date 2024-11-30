package com.exercise.jobdataapi.util;

import java.util.*;

public class HelperClass {

    public static void validateAllowedParams(Map<String, String> allParams) {
        Set<String> allowedParams = new HashSet<>(Arrays.asList(
                "salary[gte]","salary[lte]", "job_title", "gender", "fields", "sort", "sort_type"
        ));

        for (String param : allParams.keySet()) {
            if (!allowedParams.contains(param)) {
                throw new IllegalArgumentException("Invalid query parameter: " + param);
            }
        }
    }

    public static List<String> convertSnakeToCamelCase(String fields) {
        String[] snakeCaseArray = fields.split(",");
        List<String> camelCaseList = new ArrayList<>();

        for (String snakeCase : snakeCaseArray) {
            camelCaseList.add(convertToCamelCase(snakeCase));
        }

        return camelCaseList;
    }

    public static String convertToCamelCase(String snakeCase) {
        String[] words = snakeCase.split("_");
        StringBuilder camelCaseString = new StringBuilder(words[0].toLowerCase());

        for (int i = 1; i < words.length; i++) {
            camelCaseString.append(words[i].substring(0, 1).toUpperCase());
            camelCaseString.append(words[i].substring(1).toLowerCase());
        }

        return camelCaseString.toString();
    }

    public static void validateFields(String fields) {
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

    public static boolean isSparseFieldRequest(String fields, Long salaryGreaterThanOrEqual, Long salaryLessThanOrEqual, String jobTitle, String gender, String sort) {
        return Objects.nonNull(fields) && !fields.isEmpty() &&
                Objects.isNull(salaryGreaterThanOrEqual) &&
                Objects.isNull(salaryLessThanOrEqual) &&
                Objects.isNull(jobTitle) &&
                Objects.isNull(gender) &&
                Objects.isNull(sort);
    }

    public static boolean isSortRequest(String sort, Long salaryGreaterThanOrEqual,Long salaryLessThanOrEqual, String jobTitle, String gender, String fields) {
        return Objects.nonNull(sort) && !sort.isEmpty() &&
                Objects.isNull(salaryGreaterThanOrEqual) &&
                Objects.isNull(salaryLessThanOrEqual) &&
                Objects.isNull(jobTitle) &&
                Objects.isNull(gender) &&
                Objects.isNull(fields);
    }

    public static boolean isFilterRequest(Long salaryGreaterThanOrEqual, Long salaryLessThanOrEqual, String jobTitle, String gender, String fields, String sort) {
        return ((Objects.nonNull(jobTitle) && !jobTitle.isEmpty()) ||
                (Objects.nonNull(gender) && !gender.isEmpty()) ||
                Objects.nonNull(salaryGreaterThanOrEqual) ||
                Objects.nonNull(salaryLessThanOrEqual)) &&
                        Objects.isNull(fields) &&
                        Objects.isNull(sort);
    }
}
