package com.hust.interviewmanagement.entities;

import com.hust.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String note;
    private String email;
    private String cv;
    private String cvId;
    private boolean mode;
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL)
    @ToString.Exclude
    private ResultInterview resultInterview;
}
