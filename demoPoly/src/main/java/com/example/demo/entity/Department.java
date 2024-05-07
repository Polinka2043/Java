package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int employeeCount;

    @ElementCollection
    private List<String> rooms;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "department_roles",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<Staff> roles;
    @OneToOne
    //@JoinColumn(name="id", nullable = false)
    //@PrimaryKeyJoinColumn
    //@JoinFormula("(SELECT Staff.id FROM Staff WHERE Staff.position = 'head' AND Staff.address = name)")
    //@JoinColumnsOrFormulas({
    //        @JoinColumnOrFormula(formula=@JoinFormula(value="(SELECT Staff.id FROM Staff WHERE Staff.position = 'head' AND Staff.address = name)", referencedColumnName="id")),
    //        @JoinColumnOrFormula(column=@JoinColumn(name = "name", referencedColumnName="address"))
    //})
    private Staff headOfDepartment;
}
