package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
