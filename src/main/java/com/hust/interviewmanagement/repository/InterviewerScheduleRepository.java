package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.InterviewerSchedule;
import com.hust.interviewmanagement.entities.KeyInterviewerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterviewerScheduleRepository extends JpaRepository<InterviewerSchedule, KeyInterviewerSchedule> {

    List<InterviewerSchedule> findInterviewerScheduleByInterviewer_IdIn(List<Long> ids);
    @Modifying
    @Query("DELETE InterviewerSchedule where schedule.id = ?1")
    void deleteBySchedule_Id(Long id);
}
