package com.hust.interviewmanagement.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interviewer {
    private Long id;
    private String fullName;
}
