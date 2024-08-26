package com.github.mitschi.models;

import lombok.Data;
import lombok.Generated;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class Employee {
    @Generated
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private int employeeNumber;

    public Employee(){
    }

    public String getName() {
        return name;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public Long getId() {
        return id == null ? 0L : id;
    }
}
