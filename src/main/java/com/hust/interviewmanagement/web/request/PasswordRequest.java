package com.hust.interviewmanagement.web.request;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String newPasswordC;
}
