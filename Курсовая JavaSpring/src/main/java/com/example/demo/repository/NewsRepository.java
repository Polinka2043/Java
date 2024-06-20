package com.example.demo.repository;

import com.example.demo.entity.News;
import com.example.demo.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByName(String name);
    List<News> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
