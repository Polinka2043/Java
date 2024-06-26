package com.example.demo.repository;

import com.example.demo.entity.Payments;
import com.example.demo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByName(String name);
    List<Person> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
