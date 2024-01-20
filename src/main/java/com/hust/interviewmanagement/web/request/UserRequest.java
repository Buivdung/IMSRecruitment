package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EGender;
import com.hust.interviewmanagement.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long id;
    private String fullName;
    private String email;
    private LocalDate dob;
    private String phoneNumber;
    private String address;
    private ERole role;
    private EGender gender;
    private Long departmentId;
    private boolean status;
    private String notes;
}
