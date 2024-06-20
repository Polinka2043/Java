package com.example.demo.service;

import com.example.demo.entity.Human;
import com.example.demo.entity.Person;
import com.example.demo.repository.HumanRepository;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Scope("singleton")
public class HumanService {
    private final HumanRepository humanRepository;

    @Autowired
    public HumanService(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    public void saveHuman(Human human) {
        humanRepository.save(human);
    }

    public List<Human> getHumans() {
        return humanRepository.findAll();
    }


    public Human getHumanById(Long id) {
        return humanRepository.findById(id).orElse(null);
    }


    public Human updateHuman(Long id, Human human) {
        Human existingHuman = humanRepository.findById(id).orElse(null);
        if (existingHuman != null) {
            existingHuman.setName(human.getName());
            existingHuman.setShipping(human.getShipping());
            return humanRepository.save(existingHuman);
        }
        return null;
    }


    public String deleteHuman(Long id) {
        humanRepository.deleteById(id);
        return "human with ID " + id + " has been deleted.";
    }
    public List<Human> findByName(String name) {
        return humanRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Human> findAll() {
        return humanRepository.findAll();
    }
}
