package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.*;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;

import java.util.Collection;
import java.util.List;

public interface EmailService {
    void sendMailSimple(EmailDetail emailDetail) throws MessagingException;
    void sendMailHtml(EmailDetail emailDetail   ) throws MessagingException;
    void sendMailNotificationInterviewSchedule(Collection<String> email,String subject,InterviewSchedule interviewSchedule) throws MessagingException;
    void sendMailCancelInterviewSchedule(Collection<String> email,String subject,InterviewSchedule interviewSchedule) throws MessagingException;
    void sendMailChangeInterviewSchedule(Collection<String> email,String subject,InterviewSchedule interviewSchedule) throws MessagingException;
    void sendMailToUser(Account account,String password) throws MessagingException;
    void sendMailNotifyOnBoard(Collection<String> email, String subject, Offer offer) throws MessagingException;

    void sendMailNotificationResultSuccess(ResultInterview resultInterview) throws MessagingException;
    void sendMailNotificationResultFail(ResultInterview resultInterview) throws MessagingException;
}
