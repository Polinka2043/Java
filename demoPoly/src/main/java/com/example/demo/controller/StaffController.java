package com.example.demo.controller;

import com.example.demo.entity.Staff;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StaffController {
    @Autowired
    private StaffService service;

    @PostMapping("/addStaff")
    public Staff addStaff(@RequestBody Staff staff) {
        return service.saveStaff(staff);
    }

    @PostMapping("/addStaffs")
    public List<Staff> addStaffs(@RequestBody List<Staff> staffs) {
        return service.saveStaffs(staffs);
    }

    @GetMapping("/staffs")
    public List<Staff> findAllStaffs() {
        return service.getStaffs();
    }

    @GetMapping("/staff/{id}")
    public Staff findStaffById(@PathVariable Long id) {
        return service.getStaffById(id);
    }

    @GetMapping("/staff/{name}")
    public Staff findStaffByName(@PathVariable String name) {
        return service.getStaffByName(name);
    }

    @PutMapping("/updateStaff")
    public Staff updateStaff(@RequestBody Staff staff) {
        return service.updateStaff(staff);
    }

    @DeleteMapping("/deleteStaff/{id}")
    public String deleteStaff(@PathVariable Long id) {
        return service.deleteStaff(id);
    }
}
