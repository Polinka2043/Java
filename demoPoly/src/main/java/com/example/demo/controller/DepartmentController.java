package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final StaffService staffService;
    @Autowired
    public DepartmentController(DepartmentService departmentService, StaffService staffService) {
        this.departmentService = departmentService;
        this.staffService = staffService;
    }

    @PostMapping("/addDepartment")
    public Department addDepartment(@RequestBody Department department) {
        //department.setRoles(department.getRoles().stream().map(user->staffService.getStaffById(user.getId())).toList());
        department.setHeadOfDepartment(staffService.getStaffById(department.getHeadOfDepartment().getId()));
        return departmentService.saveDepartment(department);

    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getDepartments();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }
}
