package com.hust.interviewmanagement.web.request;

import com.google.gson.annotations.SerializedName;
import com.hust.interviewmanagement.enums.EStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
@Builder
public class JobImport {
    @SerializedName("Job Title")
    private String title;

    @SerializedName("Start Date")
    private LocalDate startDate;

    @SerializedName("End Date")
    private LocalDate endDate;

    @SerializedName("Salary From")
    private BigDecimal salaryFrom;

    @SerializedName("Salary To")
    private BigDecimal salaryTo;

    @SerializedName("Working Address")
    private String workingAddress;

    @SerializedName("Description")
    private String description;
    @SerializedName("Qualification")
    private String qualification;
    @SerializedName("Skills")
    private String skills;

    @SerializedName("Benefit")
    private String benefit;

    @SerializedName("Level")
    private String level;
}
