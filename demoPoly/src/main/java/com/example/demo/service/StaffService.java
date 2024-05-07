package com.example.demo.service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public List<Staff> saveStaffs(List<Staff> staffs) {
        return staffRepository.saveAll(staffs);
    }

    public List<Staff> getStaffs() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    public Staff getStaffByName(String name) {
        return staffRepository.findByName(name);
    }

    public Staff updateStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public String deleteStaff(Long id) {
        staffRepository.deleteById(id);
        return "Staff with ID " + id + " has been deleted.";
    }
}
