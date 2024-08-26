package com.github.mitschi.repositories;

import com.github.mitschi.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
