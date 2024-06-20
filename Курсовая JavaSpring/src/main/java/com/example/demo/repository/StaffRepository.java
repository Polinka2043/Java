package com.example.demo.repository;

import com.example.demo.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByName(String name);
    List<Staff> findByNameContainingIgnoreCase(String name);

    //List<Staff> findByAddress(String address);

    //List<Staff> findByPosition(String position);

    void deleteById(Long id);
}
