package com.example.demo.repository;

import com.example.demo.entity.Human;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
