package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EStatus {
    NA("N/A"),
    OPEN("Open"),
    CLOSE("Close"),
    IN_PROGRESS("In progress"),
    FAIL("Fail"),
    PASS("Pass"),
    CANCEL("Cancel"),
    BANNED("Banned"),
    WAITING_FOR_INTERVIEW("Waiting for interview"),
    CANCELED_INTERVIEW("Canceled interview"),
    PASSED_INTERVIEW("Passed interview"),
    FAILED_INTERVIEW("Failed interview"),
    WAITING_FOR_APPROVAL("Waiting for approval"),
    APPROVED_OFFER("Approved offer"),
    REJECTED_OFFER("Rejected offer"),
    WAITING_FOR_RESPONSE("Waiting for response"),
    ACCEPTED_OFFER("Accepted offer"),
    DECLINED_OFFER("Declined offer"),
    CANCELED_OFFER("Cancel offer");
    private final String value;

    EStatus(String value) {
        this.value = value;
    }

}
