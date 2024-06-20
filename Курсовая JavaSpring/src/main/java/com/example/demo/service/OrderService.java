package com.example.demo.service;

import com.example.demo.entity.OrderZak;
import com.example.demo.repository.OrderZakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class OrderService {
    private final OrderZakRepository orderRepository;

    @Autowired
    public OrderService(OrderZakRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(OrderZak order) {
        orderRepository.save(order);
    }

    public List<OrderZak> getOrders() {
        return orderRepository.findAll();
    }


    public OrderZak getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public OrderZak updateOrder(Long id, OrderZak order) {
        OrderZak existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            existingOrder.setPayments(order.getPayments());
            existingOrder.setPerson(order.getPerson());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setOrderDate(order.getOrderDate());
            existingOrder.setName(order.getName());
            return orderRepository.save(existingOrder);
        }
        return null;
    }


    public String deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return "Order with ID " + id + " has been deleted.";
    }
    public List<OrderZak> findByName(String name) {
        return orderRepository.findByNameContainingIgnoreCase(name);
    }
    public List<OrderZak> findAll() {
        return orderRepository.findAll();
    }
}
