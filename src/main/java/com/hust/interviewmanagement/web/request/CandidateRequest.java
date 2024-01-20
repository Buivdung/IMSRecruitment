package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CandidateRequest {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private MultipartFile cv;
    private Long jobId;
    private String note;
    private EStatus status;
    private Boolean mode;
}
