package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.Account;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.web.response.Interviewer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findUsersByAccount_RoleIn(List<ERole> roles);
    List<Users> findUsersByAccount_Role(ERole roles);
    @Query("SELECT DISTINCT new com.hust.interviewmanagement.web.response.Interviewer(u.id,u.fullName) FROM Users u " +
            "JOIN u.schedules s " +
            "JOIN  s.schedule is " +
            "WHERE concat( is.schedule,'') not like %?1%")
    List<Interviewer> findInterviewers(LocalDate date);
    @Query("SELECT u FROM Users u " +
            "WHERE concat(u.fullName, u.account.email, u.department.name, u.phoneNumber) LIKE %?1% " +
            "order by u.id desc ")
    Page<Users> findAll(String param,Pageable pageable);
    @Query("SELECT u FROM Users u " +
            "WHERE concat(u.fullName, u.account.email, u.department.name, u.phoneNumber) LIKE %?1% " +
            "AND u.account.role = ?2 " +
            "order by u.id desc ")
    Page<Users> findAll(String param, ERole role,Pageable pageable);
    
    Optional<Users> findByAccount_Email(String name);
    List<Users> findAllByIdIn(List<Long> ids);
}
