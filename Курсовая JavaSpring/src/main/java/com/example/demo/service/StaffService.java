package com.example.demo.service;


import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class StaffService {
    private final StaffRepository staffRepository;



    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }


    public void saveStaff(Staff staff) {
        staffRepository.save(staff);
    }


    public List<Staff> getStaffs() {
        return staffRepository.findAll();
    }


    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }


    public Staff updateStaff(Long id, Staff staff) {
        Staff existingStaff = staffRepository.findById(id).orElse(null);
        if (existingStaff != null) {
            existingStaff.setName(staff.getName());
            existingStaff.setBirthDate(staff.getBirthDate());
            existingStaff.setPosition(staff.getPosition());
            existingStaff.setShop(staff.getShop());
            return staffRepository.save(existingStaff);
        }
        return null;
    }


    public String deleteStaff(Long id) {
        staffRepository.deleteById(id);
        return "Staff with ID " + id + " has been deleted.";
    }

    public List<Staff> findByName(String name) {
        return staffRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }
}
