package com.example.demo.service;

import com.example.demo.entity.Payments;
import com.example.demo.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Scope("singleton")
public class PaymentsService {
    private final PaymentsRepository paymentsRepository;

    @Autowired
    public PaymentsService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    public void savePayments(Payments payments) {
        paymentsRepository.save(payments);
    }

    public List<Payments> getPayments() {
        return paymentsRepository.findAll();
    }


    public Payments getPaymentsById(Long id) {
        return paymentsRepository.findById(id).orElse(null);
    }


    public Payments updatePayments(Long id, Payments payments) {
        Payments existingPayments = paymentsRepository.findById(id).orElse(null);
        if (existingPayments != null) {
            existingPayments.setMethod(payments.getMethod());
            existingPayments.setAmount(payments.getAmount());
            existingPayments.setName(payments.getName());
            return paymentsRepository.save(existingPayments);
        }
        return null;
    }


    public String deletePayments(Long id) {
        paymentsRepository.deleteById(id);
        return "Payments with ID " + id + " has been deleted.";
    }
    public List<Payments> findByName(String name) {
        return paymentsRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Payments> findAll() {
        return paymentsRepository.findAll();
    }
}
