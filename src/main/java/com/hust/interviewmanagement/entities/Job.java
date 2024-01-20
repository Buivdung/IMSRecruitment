package com.hust.interviewmanagement.entities;

import com.hust.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    @Nationalized
    @Column(length = 2000)
    private String workingAddress;
    @Nationalized
    @Column(length = 2000)
    private String description;
    @Nationalized
    @Column(length = 2000)
    private String qualification;
    @Nationalized
    private String benefit;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    private LocalDate endDate;
    private LocalDate startDate;
    private Long apply;
    private Long interviewed;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createDate;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<SkillJob> skillJobs;

    @OneToMany(mappedBy = "job")
    @ToString.Exclude
    private List<Candidate> candidates;
}
