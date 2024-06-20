package com.example.demo;


import com.example.demo.entity.Customers;
import com.example.demo.entity.Role;
import com.example.demo.repository.CustomersRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringComponent
public class DataGenerator {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner loadData(CustomersRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.save(new Customers("user", passwordEncoder.encode("u"), Role.ROLE_USER));
            userRepository.save(new Customers("admin", passwordEncoder.encode("a"), Role.ROLE_ADMIN));
            userRepository.save(new Customers("viewer", passwordEncoder.encode("v"), Role.ROLE_VIEWER));
        };
    }
}