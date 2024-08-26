package com.github.mitschi.services;

import com.github.mitschi.models.Employee;
import com.github.mitschi.models.Lecture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

@Service
public class LectureService {
    @Value("${endpoint.lecture}")
    private String lectureEndpointUrl;

    WebClient webClient;

    public LectureService() {
        this.webClient = WebClient.builder().build();
    }

    HashMap<String, Long> lecture_name_id;
    HashMap<String, Long> lecture_number_id;
    HashMap<Long, List<Long>> employee_lecture_ids;

    public void reload(Lecture[] lectures) {
        lecture_name_id = new HashMap<>();
        lecture_number_id = new HashMap<>();
        for(Lecture lecture: lectures) {
            String name = lecture.getName();
            String number = lecture.getNumber();
            long id = lecture.getId();
            lecture_name_id.put(name, id);
            lecture_number_id.put(number, id);
        }
        employee_lecture_ids = new HashMap<>();
        for(Lecture lecture: lectures) {
            long employee_id = lecture.getLecturerId();
            if(!employee_lecture_ids.containsKey(employee_id)) {
                employee_lecture_ids.put(employee_id, new ArrayList<>());
            }
            employee_lecture_ids.get(employee_id).add(lecture.getId());
        }
    }

    public Flux<Lecture> getLectures(){
        Flux<Lecture> response = webClient.get()
                .uri(lectureEndpointUrl)
                .retrieve()
                .bodyToFlux(Lecture.class);

        return response;
    }
    
    public List<Long> getEmployeeLectures(long employee_id) {
        return employee_lecture_ids.getOrDefault(employee_id, new ArrayList<>());
    }

    public Mono<Lecture> addLecture(Lecture l) throws InputMismatchException {
        long employee_id = l.getLecturerId();
        int taught = employee_lecture_ids.getOrDefault(employee_id, new ArrayList<>()).size();
        if(taught < 2) {
            Mono<Lecture> response = webClient.post()
                    .uri(lectureEndpointUrl)
                    .body(BodyInserters.fromValue(l))
                    .retrieve()
                    .bodyToMono(Lecture.class);
            if(!employee_lecture_ids.containsKey(employee_id)) {
                employee_lecture_ids.put(employee_id, new ArrayList<>());
            }
            employee_lecture_ids.get(employee_id).add(l.getId());
            return response;
        } else {
            throw new InputMismatchException("This employee has taught two lectures.");
        }
    }

    public Mono<Employee> deleteLectureByName(String name) {
        long id = lecture_name_id.getOrDefault(name, 0L);
        return deleteLectureById(id);
    }

    public Mono<Employee> deleteLectureByNumber(String number) {
        long id = lecture_number_id.getOrDefault(number, 0L);
        return deleteLectureById(id);
    }

    public Mono<Employee> deleteLectureById(Long id) {
        return webClient
                .delete()
                .uri(lectureEndpointUrl + "/" + id)
                .retrieve()
                .bodyToMono(Employee.class);
    }

    public String getLectureEndpointUrl() {
        return lectureEndpointUrl;
    }
}
