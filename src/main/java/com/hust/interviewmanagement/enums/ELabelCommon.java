package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum ELabelCommon {
    MESSAGE("message"),
    ALERT("alert"),
    JOBS("jobs"),
    JOB("job"),
    JOB_REQUEST("jobRequest"),
    SKILLS("skills"),
    SKILL_IDS("skillIds"),
    LEVELS("levels"),
    DEPARTMENTS("departments"),
    CANDIDATES("candidates"),
    SEARCH_RESPONSE("searchResponse"),
    USER("user");
    private final String value;
    ELabelCommon(String value){
        this.value = value;
    };
}
