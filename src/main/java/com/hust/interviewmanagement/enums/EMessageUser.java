package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EMessageUser {

    DONT_USER("Don't found user.");
    private final String value;
    EMessageUser(String value){
        this.value = value;
    }
}
