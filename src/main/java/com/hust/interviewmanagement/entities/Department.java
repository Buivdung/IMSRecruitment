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
public class Department  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "department_name")
    private String name;

    @OneToMany(mappedBy = "department",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Users> users;

    @OneToMany(mappedBy = "department",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Offer> offers;
}
