package com.hust.interviewmanagement.web.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class CandidateResp {
    private String jobTitle;
    private List<String> interviewer;
    private String level;
    private String note;
}
