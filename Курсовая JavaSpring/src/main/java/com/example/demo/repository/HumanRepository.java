package com.example.demo.repository;

import com.example.demo.entity.Human;
import com.example.demo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {
    List<Human> findByName(String name);
    List<Human> findByNameContainingIgnoreCase(String name);
    void deleteById(Long id);
}
