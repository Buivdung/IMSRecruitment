package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EMessageCandidate {
    DONT_CANDIDATE("Don't find job");
    private final String value;
    EMessageCandidate(String value){
        this.value = value;
    };
}
