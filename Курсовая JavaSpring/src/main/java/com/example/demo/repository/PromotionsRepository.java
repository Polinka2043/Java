package com.example.demo.repository;

import com.example.demo.entity.News;
import com.example.demo.entity.Promotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long> {
    List<Promotions> findByName(String name);
    List<Promotions> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
