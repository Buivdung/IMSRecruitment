package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EContractType {
    TRAIL("Trail"),
    TRAINING("Training"),
    ONE_YEAR("One year"),
    THREE_YEAR("Three year"),
    UNLIMITED("Unlimited");
    private final String value;

    EContractType(String value) {
        this.value = value;
    }

}
