package com.github.mitschi.models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@RequiredArgsConstructor
//@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Integer employeeNumber;

    public Employee(){
    }

    public void updateFromDto(Employee other) {
        this.setName(other.getName());
        this.setEmployeeNumber(other.getEmployeeNumber());
    }
}
