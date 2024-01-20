package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EContractType;
import com.hust.interviewmanagement.enums.EPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferRequest {
    private Long id;
    private Long candidateId;
    private EPosition position;
    private Long manager;
    private EContractType contractType;
    private LocalDate contractFrom;
    private LocalDate contractTo;
    private Long department;
    private Long recruiter;
    private LocalDate dueDate;
    private BigDecimal basicSalary;
    private String notes;
}
