package com.github.mitschi.models;

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
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String number;

    @NonNull
    private Long lecturerId;

    public Lecture() {
    }

    public void updateFromDto(Lecture other) {
        this.setName(other.getName());
        this.setNumber(other.getNumber());
        this.setLecturerId(other.getLecturerId());
    }
}
