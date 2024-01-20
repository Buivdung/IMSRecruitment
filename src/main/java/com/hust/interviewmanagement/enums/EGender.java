package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EGender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String value;

    EGender(String value) {
        this.value = value;
    }

}
