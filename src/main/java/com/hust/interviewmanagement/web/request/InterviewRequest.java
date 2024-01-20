package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class InterviewRequest {
    private Long id;

    @NotBlank(message = "{check.notnull}")
    private String title;

    @NotNull(message = "{check.notnull}")
    private Long jobId;

    @NotNull(message = "{check.notnull}")
    @Positive(message = "{candidateNumber.valid}")
    private Long candidateNumber;

    @NotNull(message = "{check.notnull}")
    @FutureOrPresent(message = "{date.valid}")
    private LocalDateTime schedule;

    @NotNull(message = "{check.notnull}")
    private List<Long> interviewId;

    @NotNull(message = "{check.notnull}")
    private boolean location;

    @NotNull(message = "{check.notnull}")
    private Long recruiterId;

    @NotBlank(message = "{check.notnull}")
    @Length(max = 250, message = "{meeting.length}")
    private String meeting;

    @Length(max = 1000, message = "{description.length}")
    private String note;
    private EStatus status;
}
