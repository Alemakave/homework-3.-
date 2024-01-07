package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.HogwartsService;

import java.util.Set;

@RestController
public class HogwartsController {
    private final HogwartsService hogwartsService;

    public HogwartsController(HogwartsService hogwartsService) {
        this.hogwartsService = hogwartsService;
    }

    @GetMapping("/get-students")
    public int getStudents() {
        return hogwartsService.getStudents();
    }

    @GetMapping("/get-avg-students-age")
    public double getAvgStudentsAge() {
        return hogwartsService.getAvgStudentsAge();
    }

    @GetMapping("/get-last-five-students")
    public Set<Student> getLastFiveStudents() {
        return hogwartsService.getLastFiveStudents();
    }
}
