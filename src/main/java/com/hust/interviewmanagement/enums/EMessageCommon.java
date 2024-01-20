package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EMessageCommon {

    NO_ACTIVE("Account not activated."),
    LOGIN_FAIL("Invalid username/password. Please try again.");
    private final String value;
    EMessageCommon(String value){
        this.value = value;
    }
}