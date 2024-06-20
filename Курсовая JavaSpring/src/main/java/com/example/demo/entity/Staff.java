package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                    // Уникальный идентификатор сотрудника

    private String name;                // Имя сотрудника

    private LocalDate birthDate;        // Дата рождения сотрудника

    private String position;            // Должность сотрудника


    @OneToMany(mappedBy = "headOfShop", fetch = FetchType.EAGER)
    private List<Shop> shop;
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((birthDate == null) ? 0 : name.hashCode());
        result = prime * result + ((position == null) ? 0 : name.hashCode());
        return result;
    }
    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


