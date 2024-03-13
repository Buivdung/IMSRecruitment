package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.ResultInterview;
import com.hust.interviewmanagement.web.request.ResultRequest;
import jakarta.mail.MessagingException;

import java.util.List;

public interface ResultService {
    ResultInterview updateResult(ResultRequest resultRequest) throws MessagingException;
    ResultInterview findById(Long id);
    List<ResultInterview> findByResultPass();
    ResultInterview findResultInterviewByCandidate_Id(Long id);
}
