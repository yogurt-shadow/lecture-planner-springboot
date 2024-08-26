package com.github.mitschi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mitschi.controllers.LectureController;
import com.github.mitschi.models.validation.LectureValidator;
import com.github.mitschi.models.Lecture;
import com.github.mitschi.repositories.LectureRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;
import java.util.Optional;

/**
 * Note, when testing a REST API, the tests should focus on:
 * the HTTP response code
 * other HTTP headers in the response
 * the payload (JSON, XML)
 *
 * Each test should only focus on a single responsibility
 * Focusing on a clear separation always has benefits, but when doing
 * this kind of black box testing, it's even more important because the general
 * tendency is to write complex test scenarios in the very beginning.
 */

@WebMvcTest(LectureController.class)
public class LectureControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LectureRepository repository;
    @MockBean
    private LectureValidator validator;

    @Test
    void getLectureById() throws Exception {
        Lecture lecture = new Lecture("Software Testing", "100.000", 1L);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(lecture));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/lectures/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Software Testing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("100.000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lecturerId").value(1L));
    }

    @Test
    public void createLecture() throws Exception {
        Lecture lecture = new Lecture("Software Testing", "100.000", 1L);
        Mockito.when(validator.isLectureValid(Mockito.any(Lecture.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/lectures")
                .content(asJsonString(lecture))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Software Testing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("100.000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lecturerId").value(1L));
    }

    @Test
    public void updateLecture() throws Exception {
        Lecture lecture = new Lecture("Software Testing", "100.000", 1L);
        Mockito.when(validator.isLectureValid(Mockito.any(Lecture.class))).thenReturn(true);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(lecture));
        Mockito.when(repository.save(Mockito.any(Lecture.class))).thenReturn(lecture);

        mockMvc.perform(MockMvcRequestBuilders.put("/lectures/1")
                        .content(asJsonString(lecture))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Software Testing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("100.000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lecturerId").value(1L));
    }

    @Test
    public void deleteLecture() throws Exception {
        Lecture lecture = new Lecture("Software Testing", "100.000", 1L);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(lecture));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/lectures/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
