package com.hust.interviewmanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(KeyInterviewerSchedule.class)
public class InterviewerSchedule implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Users interviewer;

    @Id
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private InterviewSchedule schedule;

}
