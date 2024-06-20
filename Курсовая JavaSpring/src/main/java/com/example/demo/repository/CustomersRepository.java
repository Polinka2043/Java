package com.example.demo.repository;

import com.example.demo.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Integer> {

    Customers getByUsername(String username);

    Customers getByActivationCode(String activationCode);
    Optional<Customers> findByUsername(String username);
    Customers findByUsernameContainingIgnoreCase(String username);

}