package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Department;
import com.hust.interviewmanagement.repository.DepartmentRepository;
import com.hust.interviewmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public List<Department> findAllDepartment() {
        return departmentRepository.findAll();
    }
}
