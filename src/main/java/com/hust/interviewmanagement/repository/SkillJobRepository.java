package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.KeySkillJob;
import com.hust.interviewmanagement.entities.Skill;
import com.hust.interviewmanagement.entities.SkillJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkillJobRepository extends JpaRepository<SkillJob, KeySkillJob> {
    @Modifying
    @Query("DELETE SkillJob WHERE job.id = ?1")
    void deleteSkillJob(Long id);

}
