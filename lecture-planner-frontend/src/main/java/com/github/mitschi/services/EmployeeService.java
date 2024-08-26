package com.github.mitschi.services;

import com.github.mitschi.models.Employee;
import com.github.mitschi.models.Lecture;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class EmployeeService {

    @Value("${endpoint.employee}")
    private String employeesEndpointUrl;

    WebClient webClient;

    public EmployeeService() {
        this.webClient = WebClient.builder().build();
    }

    public Flux<Employee> getEmployees(){
        Flux<Employee> response = webClient.get()
                .uri(employeesEndpointUrl)
                .retrieve()
                .bodyToFlux(Employee.class);

        return response;
    }

    public Flux<Employee> getEmployeeById(Long id) {
        return webClient.get()
                .uri(employeesEndpointUrl + "/" + id)
                .retrieve()
                .bodyToFlux(Employee.class);
    }

    public long getIdByName(String name) {
        return employee_name_id.getOrDefault(name, 0L);
    }

    public long getIdByNumber(int number) {
        return employee_number_id.getOrDefault(number, 0L);
    }

    HashMap<String, Long> employee_name_id;
    HashMap<Integer, Long> employee_number_id;
    public void reload(Employee[] employees) {
        employee_name_id = new HashMap<>();
        employee_number_id = new HashMap<>();
        for(Employee employee: employees) {
            String name = employee.getName();
            int number = employee.getEmployeeNumber();
            long id = employee.getId();
            employee_name_id.put(name, id);
            employee_number_id.put(number, id);
        }
    }

    public Mono<Employee> addEmployee(Employee e) {
        Mono<Employee> response = webClient.post()
                .uri(employeesEndpointUrl)
                .body(BodyInserters.fromValue(e))
                .retrieve()
                .bodyToMono(Employee.class);
        return response;
    }

    public Mono<Employee> deleteEmployeeByName(String name) {
        long id = employee_name_id.getOrDefault(name, 0L);
        return deleteEmployeeById(id);
    }

    public Mono<Employee> deleteEmployeeByNumber(int number) {
        long id = employee_number_id.getOrDefault(number, 0L);
        return deleteEmployeeById(id);
    }

    public Mono<Employee> deleteEmployeeById(Long id) {
        Mono<Employee> response = webClient
                .delete()
                .uri(employeesEndpointUrl + "/" + id)
                .retrieve()
                .bodyToMono(Employee.class);
        return response;
    }

    public String getEmployeesEndpointUrl() {
        return employeesEndpointUrl;
    }
}
