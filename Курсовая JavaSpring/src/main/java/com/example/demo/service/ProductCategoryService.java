package com.example.demo.service;

import com.example.demo.entity.ProductCategory;
import com.example.demo.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public void saveProductCategory(ProductCategory product) {
        productCategoryRepository.save(product);
    }

    public List<ProductCategory> getProductCategorys() {
        return productCategoryRepository.findAll();
    }


    public ProductCategory getProductCategoryById(Long id) {
        return productCategoryRepository.findById(id).orElse(null);
    }


    public ProductCategory updateProductCategory(Long id, ProductCategory product) {
        ProductCategory existingProduct = productCategoryRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setComment(product.getComment());
            existingProduct.setProduct(product.getProduct());
            return productCategoryRepository.save(existingProduct);
        }
        return null;
    }

    public String deleteProductCategory(Long id) {
        productCategoryRepository.deleteById(id);
        return "ProductCategory with ID " + id + " has been deleted.";
    }
    public List<ProductCategory> findByName(String name) {
        return productCategoryRepository.findByNameContainingIgnoreCase(name);
    }
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }
}
