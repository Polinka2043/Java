package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String name;

    private String address;

    private LocalDate birthDate;

    private String position;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "staff_roles",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private List<Department> roles;
    //@ElementCollection
    //private List<String> organization;

    //@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="staff")
    //@MapsId
    //@JoinColumn(name = "id")
    //@ManyToOne
    //@JoinColumn(name = "id")
    //private Department nameDepartment;

}
