package com.hust.interviewmanagement.entities;

import com.hust.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultInterview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private EStatus result;
    private String note;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id")
    private Candidate candidate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interview_schedule_id", referencedColumnName = "interview_schedule_id")
    private InterviewSchedule interviewSchedule;

    @OneToOne(mappedBy = "resultInterview", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Offer offer;
}
