package com.hust.interviewmanagement.entities;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class KeyInterviewerSchedule implements Serializable {
    private Users interviewer;
    private InterviewSchedule schedule;

}
