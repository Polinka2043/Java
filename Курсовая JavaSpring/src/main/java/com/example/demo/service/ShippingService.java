package com.example.demo.service;

import com.example.demo.entity.OrderZak;
import com.example.demo.entity.Shipping;
import com.example.demo.repository.OrderZakRepository;
import com.example.demo.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Scope("singleton")
public class ShippingService {
    private final ShippingRepository shippingRepository;

    @Autowired
    public ShippingService(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    public void saveShipping(Shipping shipping) {
        shippingRepository.save(shipping);
    }

    public List<Shipping> getShippings() {
        return shippingRepository.findAll();
    }


    public Shipping getShippingById(Long id) {
        return shippingRepository.findById(id).orElse(null);
    }


    public Shipping updateShipping(Long id, Shipping shipping) {
        Shipping existingShipping = shippingRepository.findById(id).orElse(null);
        if (existingShipping != null) {
            existingShipping.setDelivery(shipping.getDelivery());
            existingShipping.setHuman(shipping.getHuman());
            existingShipping.setOrderDate(shipping.getOrderDate());
            return shippingRepository.save(existingShipping);
        }
        return null;
    }


    public String deleteShipping(Long id) {
        shippingRepository.deleteById(id);
        return "Shipping with ID " + id + " has been deleted.";
    }
    public List<Shipping> findByName(String name) {
        return shippingRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Shipping> findAll() {
        return shippingRepository.findAll();
    }
}
