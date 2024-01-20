package com.hust.interviewmanagement.web.request;


import com.hust.interviewmanagement.enums.EStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class JobRequest {

    private Long id;

    @NotBlank(message = "{check.notnull}")
    private String title;

    @NotNull(message = "{check.notnull}")
    @Positive(message = "{salaryFrom.valid}")
    private BigDecimal salaryFrom;

    @NotNull(message = "{check.notnull}")
    @Positive(message = "{salaryTo.valid}")
    private BigDecimal salaryTo;

    @NotBlank(message = "{check.notnull}")
    @Length(max = 250, message = "{workingAddress.length}")
    private String workingAddress;

    @Length(max = 1000, message = "{description.length}")
    private String description;

    @NotBlank(message = "{check.notnull}")
    private String qualification;

    @NotBlank(message = "{check.notnull}")
    private String benefit;

    private EStatus status;

    @NotNull(message = "{check.notnull}")
    @FutureOrPresent(message = "{date.valid}")
    private LocalDate endDate;

    @NotNull(message = "{check.notnull}")
    @FutureOrPresent(message = "{date.valid}")
    private LocalDate startDate;

    private Long apply;

    private LocalDate createDate;

    @NotNull(message = "{check.notnull}")
    private Long level;

    @NotNull(message = "{check.notnull}")
    private List<Long> skills;
}
