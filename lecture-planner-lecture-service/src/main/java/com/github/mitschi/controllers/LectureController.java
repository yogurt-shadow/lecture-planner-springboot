package com.github.mitschi.controllers;

import com.github.mitschi.models.Lecture;
import com.github.mitschi.models.validation.LectureValidator;
import com.github.mitschi.repositories.LectureRepository;
import com.github.mitschi.util.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/lectures")
@Slf4j
public class LectureController {
    private final LectureRepository lectureDao;
    private final LectureValidator validator;

    @GetMapping()
    public List<Lecture> listLectures() {
        return lectureDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lecture> getLectureById(@PathVariable("id") Long id) {
        try {
            Lecture lecture = lectureDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecture not found for id: " + id));
            return ResponseEntity.ok(lecture);
        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture Not Found", exc);
        }
    }

    @PostMapping()
    public ResponseEntity<Lecture> createLecture(@RequestBody Lecture lecture) {
        if (!validator.isLectureValid(lecture))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecture values are not valid");

        lectureDao.save(lecture);
        return new ResponseEntity<>(lecture, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lecture> updateLecture(@PathVariable("id") Long id,
                                                 @RequestBody Lecture lectureDto) {
        if (!validator.isLectureValid(lectureDto))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecture values are not valid");

        try {
            Lecture origLecture = lectureDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecture not found for id: " + id));

            origLecture.updateFromDto(lectureDto);
            Lecture updatedLecture = lectureDao.save(origLecture);
            return ResponseEntity.ok(updatedLecture);
        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture Not Found", exc);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Lecture> deleteLectureById(@PathVariable("id") Long id) {
        try {
            ResponseEntity<Lecture> response = getLectureById(id);
            lectureDao.delete(response.getBody());
            return response;
        } catch(ResponseStatusException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture Not Found", exc);
        }
    }
}
