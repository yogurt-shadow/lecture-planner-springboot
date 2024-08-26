package com.github.mitschi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mitschi.controllers.EmployeeController;
import com.github.mitschi.models.Employee;
import com.github.mitschi.models.validation.EmployeeValidator;
import com.github.mitschi.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository repository;
    @MockBean
    private EmployeeValidator validator;

    @Test
    void getEmployeeById() throws Exception {
        Employee employee = new Employee("Brian Foo", 100);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(employee));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Brian Foo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeNumber").value(100));
    }

    @Test
    public void createEmployee() throws Exception {
        Employee employee = new Employee("Brian Foo", 100);
        Mockito.when(validator.isEmployeeValid(Mockito.any(Employee.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .content(asJsonString(employee))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Brian Foo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeNumber").value(100));
    }

    @Test
    public void updateEmployee() throws Exception {
        Employee employee = new Employee("Brian Foo", 100);
        Mockito.when(validator.isEmployeeValid(Mockito.any(Employee.class))).thenReturn(true);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(employee));
        Mockito.when(repository.save(Mockito.any(Employee.class))).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/1")
                        .content(asJsonString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Brian Foo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeNumber").value(100));
    }

    @Test
    public void deleteEmployee() throws Exception {
        Employee employee = new Employee("Brian Foo", 100);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(employee));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1")
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
