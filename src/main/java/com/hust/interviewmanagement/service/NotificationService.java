package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.entities.InterviewSchedule;
import com.hust.interviewmanagement.entities.Notification;
import com.hust.interviewmanagement.web.request.CandidateRequest;
import com.hust.interviewmanagement.web.request.InterviewRequest;

import java.util.List;

public interface NotificationService {
    void NotificationAddCandidate(Candidate candidate);

    List<Notification> getNotifications(Long userId);

    void checked(Long id);

    void NotificationAddInterviewSchedule(Long id, InterviewRequest interviewRequest);
}
