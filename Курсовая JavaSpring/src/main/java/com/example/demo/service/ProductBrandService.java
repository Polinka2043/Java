package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductBrand;
import com.example.demo.repository.ProductBrandRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class ProductBrandService {
    private final ProductBrandRepository productBrandRepository;

    @Autowired
    public ProductBrandService(ProductBrandRepository productBrandRepository) {
        this.productBrandRepository = productBrandRepository;
    }

    public void saveProductBrand(ProductBrand product) {
        productBrandRepository.save(product);
    }

    public List<ProductBrand> getProductBrands() {
        return productBrandRepository.findAll();
    }


    public ProductBrand getProductBrandById(Long id) {
        return productBrandRepository.findById(id).orElse(null);
    }


    public ProductBrand updateProductBrand(Long id, ProductBrand product) {
        ProductBrand existingProduct = productBrandRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setComment(product.getComment());
            existingProduct.setProduct(product.getProduct());
            return productBrandRepository.save(existingProduct);
        }
        return null;
    }

    // Удаляет отдел по его ID и возвращает сообщение об успешном удалении
    public String deleteProductBrand(Long id) {
        productBrandRepository.deleteById(id);
        return "ProductBrand with ID " + id + " has been deleted.";
    }
    public List<ProductBrand> findByName(String name) {
        return productBrandRepository.findByNameContainingIgnoreCase(name);
    }
    public List<ProductBrand> findAll() {
        return productBrandRepository.findAll();
    }
}
