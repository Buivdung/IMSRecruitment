package com.hust.interviewmanagement.repository;


import com.hust.interviewmanagement.entities.Job;
import com.hust.interviewmanagement.entities.Level;
import com.hust.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("from Job j WHERE j.title like %?1% and j.status = ?2")
    Page<Job> findAllJob(String jobTitle, EStatus status, Pageable pageable);

    @Query("from Job j WHERE j.title like %?1%")
    Page<Job> findAllJob(String jobTitle, Pageable pageable);
    @Query("SELECT DISTINCT j from Job j " +
            "JOIN j.skillJobs sk " +
            "where concat( sk.skill.name, j.title) like %?1% " +
            "AND j.level.name like %?2% " +
            "AND j.workingAddress like %?3%")
    Page<Job> findAllJob(String param, String levelName, String address, Pageable pageable);

    @Query("SELECT j FROM Job j " +
            "WHERE j.status = ?1 ")
    List<Job> findAllByStatus(EStatus status);
    @Query("SELECT j FROM Job j " +
            "WHERE j.status = ?1 " +
            "AND j.interviewed < j.apply")
    List<Job> findAllByStatusAndApply(EStatus status);

    boolean existsJobByTitleAndLevelAndStatus(String title, Level level,EStatus status);
    boolean existsJobByTitleAndLevel_IdAndStatusAndIdNot(String title,Long levelId,EStatus status, Long id);
}
