package com.example.demo.repository;

import com.example.demo.entity.Department;
import com.example.demo.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);
    Department findByEmployeeCount(int employeeCount);
    void deleteById(Long id);
}