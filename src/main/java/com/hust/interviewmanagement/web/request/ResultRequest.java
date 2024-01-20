package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EStatus;
import lombok.Data;

@Data
public class ResultRequest {
    private Long id;
    private EStatus result;
    private String note;
}
