package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum ERole {
    ROLE_RECRUITER("Recruiter"),
    ROLE_INTERVIEW("Interview"),
    ROLE_ADMIN("Admin"),
    ROLE_MANAGER("Manager");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

}
