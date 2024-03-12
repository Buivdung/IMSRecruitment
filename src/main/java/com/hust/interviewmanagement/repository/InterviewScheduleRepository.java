package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.InterviewSchedule;
import com.hust.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    @Query("SELECT distinct i FROM InterviewSchedule i " +
            "JOIN i.resultInterviews ri " +
            "JOIN i.interviewer iv " +
            "WHERE concat(i.title, ri.candidate.fullName) like %?1% " +
            "AND iv.interviewer.fullName like %?2% " +
            "ORDER BY i.schedule desc " )
    Page<InterviewSchedule> findAll(String param, String interviewer, Pageable pageable);

    @Query("SELECT distinct i FROM InterviewSchedule i " +
            "JOIN i.resultInterviews ri " +
            "JOIN i.interviewer iv " +
            "WHERE concat(i.title, ri.candidate.fullName) like %?1% " +
            "AND iv.interviewer.fullName like %?2% " +
            "AND i.status = ?3 " +
            "ORDER BY i.schedule desc ")
    Page<InterviewSchedule> findAll(String param, String interviewer, EStatus status, Pageable pageable);

    //    @Query(value = "SELECT * FROM interview_schedule i  " +
//            "WHERE CONVERT(date,i.schedule) < CONVERT(date,GETDATE())",nativeQuery = true)
//    List<InterviewSchedule> findAllBySchedule();
    @Query("select i from InterviewSchedule i " +
            "where concat(i.schedule,'') like %?1% AND i.status = 'OPEN'")
    List<InterviewSchedule> findAllBySchedule(LocalDate localDate);
    @Modifying
    @Query("delete InterviewSchedule where id = ?1")
    void deleteByInterviewScheduleID(Long id);
}
