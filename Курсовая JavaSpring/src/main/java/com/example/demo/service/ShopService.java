package com.example.demo.service;

import com.example.demo.entity.Shop;
import com.example.demo.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class ShopService {
    private final ShopRepository shopRepository;

    @Autowired
    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public void saveShop(Shop shop) {
        shopRepository.save(shop);
    }

    public List<Shop> getShops() {
        return shopRepository.findAll();
    }


    public Shop getShopById(Long id) {
        return shopRepository.findById(id).orElse(null);
    }


    public Shop updateShop(Long id, Shop shop) {
        Shop existingShop = shopRepository.findById(id).orElse(null);
        if (existingShop != null) {
            existingShop.setName(shop.getName());
            existingShop.setAddress(shop.getAddress());
            return shopRepository.save(existingShop);
        }
        return null;
    }


    public String deleteShop(Long id) {
        shopRepository.deleteById(id);
        return "Shop with ID " + id + " has been deleted.";
    }
    public List<Shop> findByName(String name) {
        return shopRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Shop> findAll() {
        return shopRepository.findAll();
    }
}
