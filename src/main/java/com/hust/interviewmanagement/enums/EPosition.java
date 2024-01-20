package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EPosition {

    BACKEND_DEVELOPER("Backend Developer"),
    BUSINESS_ANALYST("Business Analyst"),
    TESTER("Tester"),
    HR("HR"),
    PROJECT_MANAGER("Project manager");

    private final String value;

    EPosition(String value) {
        this.value = value;
    }

}
