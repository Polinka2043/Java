package com.example.demo.repository;

import com.example.demo.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    List<Shipping> findByName(String name);
    List<Shipping> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
