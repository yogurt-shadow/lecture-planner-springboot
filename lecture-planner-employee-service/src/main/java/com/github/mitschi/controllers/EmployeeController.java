package com.github.mitschi.controllers;

import com.github.mitschi.models.Employee;
import com.github.mitschi.models.validation.EmployeeValidator;
import com.github.mitschi.repositories.EmployeeRepository;
import com.github.mitschi.util.IllegalResourceException;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
@Slf4j
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeValidator validator;

    @GetMapping()
    public List<Employee> listEmployees() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        try {
            Employee employee = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for id: " + id));
            return ResponseEntity.ok(employee);
        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found", exc);
        }
    }

    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        if (!validator.isEmployeeValid(employee))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee values are not valid");

        repository.save(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id,
                                                   @RequestBody Employee employee) {
        if (!validator.isEmployeeValid(employee))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee values are not valid");

        try {
            Employee origEmployee = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for id: " + id));

            origEmployee.updateFromDto(employee);
            Employee updatedEmployee = repository.save(origEmployee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found", exc);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable("id") Long id) {
        try {
            ResponseEntity<Employee> response = getEmployeeById(id);
            repository.delete(response.getBody());
            return response;
        } catch(ResponseStatusException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found", exc);
        }
    }
}
