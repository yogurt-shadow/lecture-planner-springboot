package com.github.mitschi.models;

import lombok.Data;
import lombok.Generated;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Lecture {
    @Generated
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String number;

    @NonNull
    private Long lecturerId;

    public Lecture(){
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Long getLecturerId() {
        return lecturerId;
    }

    public Long getId() {
        return id == null ? 0L : id;
    }
}
