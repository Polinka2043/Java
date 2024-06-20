package com.example.demo.service;


import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Scope("singleton")
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public List<Person> getPersons() {
        return personRepository.findAll();
    }


    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }


    public Person updatePerson(Long id, Person person) {
        Person existingPerson = personRepository.findById(id).orElse(null);
        if (existingPerson != null) {
            existingPerson.setName(person.getName());
            existingPerson.setOrderzak(person.getOrderzak());
            return personRepository.save(existingPerson);
        }
        return null;
    }


    public String deletePerson(Long id) {
        personRepository.deleteById(id);
        return "Person with ID " + id + " has been deleted.";
    }
    public List<Person> findByName(String name) {
        return personRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Person> findAll() {
        return personRepository.findAll();
    }
}
