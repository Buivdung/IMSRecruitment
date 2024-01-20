package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.enums.EStatus;
import lombok.Data;

import java.util.List;
@Data
public class JobSearch {
    private String param;
    private String level;
    private String address;
    private Integer pageNumber;
    public static final Integer PAGE_SIZE = 2;
    private List<Integer> pageMaxNumber;
    private String message;


}
