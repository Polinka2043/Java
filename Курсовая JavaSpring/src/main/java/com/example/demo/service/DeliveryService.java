package com.example.demo.service;

import com.example.demo.entity.Delivery;
import com.example.demo.entity.Human;
import com.example.demo.repository.DeliveryRepository;
import com.example.demo.repository.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Scope("singleton")
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void saveDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    public List<Delivery> getDelivers() {
        return deliveryRepository.findAll();
    }


    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }


    public Delivery updateDelivery(Long id, Delivery delivery) {
        Delivery existingDelivery = deliveryRepository.findById(id).orElse(null);
        if (existingDelivery != null) {
            existingDelivery.setName(delivery.getName());
            existingDelivery.setTime(delivery.getTime());
            existingDelivery.setShipping(delivery.getShipping());
            return deliveryRepository.save(existingDelivery);
        }
        return null;
    }


    public String deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
        return "Delivery with ID " + id + " has been deleted.";
    }
    public List<Delivery> findByName(String name) {
        return deliveryRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }
}
