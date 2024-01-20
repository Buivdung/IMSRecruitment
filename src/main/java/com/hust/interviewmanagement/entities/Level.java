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
public class Level  implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Long id;

    @Column(name = "level_name")
    private String name;

    @OneToMany(mappedBy = "level")
    @ToString.Exclude
    private List<Job> jobs;

}
