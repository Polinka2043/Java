package com.example.demo.controller;

import com.example.demo.entity.Organisation;
import com.example.demo.service.OrganisationService;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/Organisations")
public class OrganisationController {

    private final OrganisationService organisationService;
    private final StaffService staffService;

    @Autowired
    public OrganisationController(OrganisationService organisationService, StaffService staffService) {
        this.organisationService = organisationService;
        this.staffService = staffService;
    }


    @PostMapping("/addOrganisation")
    public Organisation addOrganisation(@RequestBody Organisation organisation) {
        //organisation.setUsers(organisation.getUsers().stream().map(user->staffService.getStaffById(user.getId())).toList());
        //organisation.setUsers(staffService.getStaffById(organisation.getUsers().getId()));
        return organisationService.addOrganisation(organisation);
    }

    @DeleteMapping("/deleteOrganisation/{id}")
    public void deleteOrganisation(@PathVariable Long id) {
        organisationService.deleteOrganisation(id);
    }

    @GetMapping("/Organisations")
    public List<Organisation> getAllOrganisations() {
        return organisationService.getAllOrganisations();
    }
    @GetMapping("/Organisation/{id}")
    public Organisation findOrganisationById(@PathVariable Long id) {
        return organisationService.getOrganisationById(id);
    }
}
