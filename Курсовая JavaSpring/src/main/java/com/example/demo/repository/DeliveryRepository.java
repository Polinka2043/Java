package com.example.demo.repository;

import com.example.demo.entity.Delivery;
import com.example.demo.entity.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByName(String name);
    List<Delivery> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
