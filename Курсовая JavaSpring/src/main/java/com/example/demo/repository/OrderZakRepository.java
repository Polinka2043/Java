package com.example.demo.repository;

import com.example.demo.entity.OrderZak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderZakRepository extends JpaRepository<OrderZak, Long> {
    List<OrderZak> findByName(String name);
    List<OrderZak> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
