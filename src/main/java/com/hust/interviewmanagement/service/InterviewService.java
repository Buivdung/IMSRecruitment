package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.InterviewSchedule;
import com.hust.interviewmanagement.web.request.InterviewRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

public interface InterviewService {
    InterviewSchedule saveInterviewSchedule(InterviewRequest interviewRequest) throws MessagingException;
    Page<InterviewSchedule> findAllInterviewSchedule(SearchRequest searchRequest);
    InterviewSchedule findInterviewScheduleById(Long id);

    InterviewSchedule updateInterviewSchedule(InterviewRequest interviewRequest) throws MessagingException;
    void deleteInterviewSchedule(Long id) throws MessagingException;
}
