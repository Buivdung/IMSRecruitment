package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.enums.EStatus;
import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private String param;
    private ERole role;
    private EStatus status;
    private String interviewer;
    private String department;
    private Integer pageNumber;
    public static final Integer PAGE_SIZE = 2;
    private List<Integer> pageMaxNumber;
    private String message;
}
