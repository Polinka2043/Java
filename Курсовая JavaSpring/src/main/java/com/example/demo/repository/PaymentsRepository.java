package com.example.demo.repository;

import com.example.demo.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {
    List<Payments> findByName(String name);
    List<Payments> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
