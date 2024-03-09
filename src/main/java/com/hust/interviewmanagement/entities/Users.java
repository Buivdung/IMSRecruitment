package com.hust.interviewmanagement.entities;

import com.hust.interviewmanagement.enums.EGender;
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
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String fullName;
    private LocalDate dob;
    private String phoneNumber;
    private String address;
    @Enumerated(EnumType.STRING)
    private EGender gender;
    private String notes;
    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Account account;


    @OneToMany(mappedBy = "recruiter",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InterviewSchedule> interviewScheduleRecruiter;

    @OneToMany(mappedBy = "interviewer")
    @ToString.Exclude
    private List<InterviewerSchedule> schedules;

    @OneToMany(mappedBy = "recruiter" ,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Offer> offerRecruiter;

    @OneToMany(mappedBy = "manager" ,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Offer> offerManager;


}
