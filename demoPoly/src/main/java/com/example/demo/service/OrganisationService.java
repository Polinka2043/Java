package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.Organisation;
import com.example.demo.entity.Staff;
import com.example.demo.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    @Autowired
    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    public Organisation addOrganisation(Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    public void deleteOrganisation(Long id) {
        organisationRepository.deleteById(id);
    }

    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }

    public Organisation  getOrganisationById(Long id) {
        return organisationRepository.findById(id).orElse(null);
    }
}