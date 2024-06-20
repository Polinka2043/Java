package com.example.demo.repository;

import com.example.demo.entity.ProductBrand;
import com.example.demo.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByName(String name);
    List<ProductCategory> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
