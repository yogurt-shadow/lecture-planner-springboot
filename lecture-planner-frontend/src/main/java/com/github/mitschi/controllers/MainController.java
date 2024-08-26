package com.github.mitschi.controllers;

import com.github.mitschi.models.Employee;
import com.github.mitschi.models.Lecture;
import com.github.mitschi.services.EmployeeService;
import com.github.mitschi.services.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final EmployeeService employeeService;
    private final LectureService lectureService;

    @GetMapping()
    public String main(Model model){
        System.out.println("Using employee endpoint " + employeeService.getEmployeesEndpointUrl());
        System.out.println("Using lecture endpoint " + lectureService.getLectureEndpointUrl());

        Employee[] employees = employeeService.getEmployees().collectList().block().toArray(new Employee[]{});
        Lecture[] lectures = lectureService.getLectures().collectList().block().toArray(new Lecture[]{});

        model.addAttribute("employees", employees);
        model.addAttribute("lectures", lectures);

        reload();
        return "index";
    }

    private void reload() {
        Employee[] employees = employeeService.getEmployees()
                .collectList().block().toArray(new Employee[]{});
        Lecture[] lectures = lectureService.getLectures()
                .collectList().block().toArray(new Lecture[]{});
        employeeService.reload(employees);
        lectureService.reload(lectures);
    }

    @GetMapping("/addEmployee")
    public RedirectView addEmployee(@RequestParam("pEmpName") String name,
                                    @RequestParam("pEmpNum") int num,
                                    Model model){
        reload();
        Employee e = new Employee(name, num);
        employeeService.addEmployee(e).block();
        // redirect back to home
        RedirectView r = new RedirectView("/");
        return r;
    }

    @GetMapping("/deleteEmployeeByName")
    public RedirectView deleteEmployeeByName(@RequestParam("pLecName") String name, Model model) {
        reload();
        employeeService.deleteEmployeeByName(name).block();
        long id = employeeService.getIdByName(name);
        for(Long lecture_id: lectureService.getEmployeeLectures(id)) {
            lectureService.deleteLectureById(lecture_id).block();
        }
        return new RedirectView("/");
    }

    @GetMapping("/deleteEmployeeByNumber")
    public RedirectView deleteEmployeeByNumber(@RequestParam("pLecNumber") String number, Model model) {
        reload();
        employeeService.deleteEmployeeByNumber(Integer.valueOf(number)).block();
        long id = employeeService.getIdByNumber(Integer.valueOf(number));
        for(Long lecture_id: lectureService.getEmployeeLectures(id)) {
            lectureService.deleteLectureById(lecture_id).block();
        }
        return new RedirectView("/");
    }

    @GetMapping("/deleteAllEmployees")
    public RedirectView deleteAllEmployees(Model model) {
        reload();
        Employee[] employees = employeeService.getEmployees().collectList().block().toArray(new Employee[]{});
        for(Employee employee: employees) {
            deleteEmployeeByName(employee.getName(), model);
        }
        return new RedirectView("/");
    }

    @GetMapping("/deleteLectureByName")
    public RedirectView deleteLectureByName(@RequestParam("pLecName") String name, Model model) {
        reload();
        lectureService.deleteLectureByName(name).block();
        return new RedirectView("/");
    }

    @GetMapping("/deleteLectureByNumber")
    public RedirectView deleteLectureByNumber(@RequestParam("pLecNumber") String number, Model model) {
        reload();
        lectureService.deleteLectureByNumber(number).block();
        return new RedirectView("/");
    }

    @GetMapping("/deleteAllLectures")
    public RedirectView deleteAllLectures(Model model) {
        reload();
        Lecture[] lectures = lectureService.getLectures().collectList().block().toArray(new Lecture[]{});
        for(Lecture lecture: lectures) {
            deleteLectureByName(lecture.getName(), model);
        }
        return new RedirectView("/");
    }

    @GetMapping("/addLecture")
    public RedirectView addLecture(@RequestParam("pLecName") String name,
                                    @RequestParam("pLecNum") String num,
                                    @RequestParam("pLecEmpId") long empId,
                                    Model model){
        reload();
        Lecture l = new Lecture(name, num, empId);
        try {
            lectureService.addLecture(l).block();
        } catch(InputMismatchException exc) {
            System.out.println("This lecture's employee teaches other two lectures.");
        }
        return new RedirectView("/");
    }
}