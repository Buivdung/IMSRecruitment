package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EMessageJob {
    CREATE_JOB_FAIL("Failed to created job"),
    CREATE_JOB_SUCCESS("Successfully created job"),
    EDIT_JOB_FAIL("Failed to updated job"),
    EDIT_JOB_SUCCESS("Successfully updated job"),

    DELETE_JOB_SUCCESS("Successfully deleted job"),
    DELETE_JOB_FAIL("Failed to delete this job"),
    DONT_JOB("Don't find job"),
    M_SALARY("Salary To must be greater than Salary From"),
    M_DATE("End date must be after or equal to start date"),
    DUPLICATE_JOB("Job already existed!");
    private final String value;

    EMessageJob(String value) {
        this.value = value;
    }

    ;
}
