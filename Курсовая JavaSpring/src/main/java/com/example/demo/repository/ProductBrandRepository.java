package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {
    List<ProductBrand> findByName(String name);
    List<ProductBrand> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
