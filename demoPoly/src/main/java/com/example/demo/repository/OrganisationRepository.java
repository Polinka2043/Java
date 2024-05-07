package com.example.demo.repository;

import com.example.demo.entity.Department;
import com.example.demo.entity.Organisation;
import com.example.demo.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    //Organisation findByName(String name);
    //Optional<Organisation> findById(Long id);
}
