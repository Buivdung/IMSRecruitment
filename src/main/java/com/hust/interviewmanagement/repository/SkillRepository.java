package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByNameIn(List<String> skillNames);
}
