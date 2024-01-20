package com.hust.interviewmanagement.entities;

import com.hust.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_schedule_id")
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    private boolean location;
    private String meeting;
    private Long candidateNumber;
    private LocalDateTime schedule;
    private String note;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<InterviewerSchedule> interviewer;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Users recruiter;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @CreationTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;

    @OneToMany(mappedBy = "interviewSchedule",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ResultInterview> resultInterviews;
}
