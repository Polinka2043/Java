package com.example.demo.service;

import com.example.demo.entity.News;
import com.example.demo.entity.Promotions;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PromotionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class PromotionsService {
    private final PromotionsRepository promotionsRepository;

    @Autowired
    public PromotionsService(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    public void savePromotions(Promotions promotions) {
        promotionsRepository.save(promotions);
    }

    public List<Promotions> getPromotions() {
        return promotionsRepository.findAll();
    }


    public Promotions getPromotionsById(Long id) {
        return promotionsRepository.findById(id).orElse(null);
    }


    public Promotions updatePromotions(Long id, Promotions promotions) {
        Promotions existingPromotions = promotionsRepository.findById(id).orElse(null);
        if (existingPromotions != null) {
            existingPromotions.setName(promotions.getName());
            existingPromotions.setDiscount(promotions.getDiscount());
            existingPromotions.setStartDate(promotions.getStartDate());
            return promotionsRepository.save(existingPromotions);
        }
        return null;
    }


    public String deleteNews(Long id) {
        promotionsRepository.deleteById(id);
        return "Promotions with ID " + id + " has been deleted.";
    }
    public List<Promotions> findByName(String name) {
        return promotionsRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Promotions> findAll() {
        return promotionsRepository.findAll();
    }
}
