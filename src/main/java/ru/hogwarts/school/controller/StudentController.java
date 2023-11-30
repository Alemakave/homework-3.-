package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Student addStudent(Student student) {
        return service.addStudent(student);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id) {
        Student foundedStudent = service.getStudent(id);

        if (foundedStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(foundedStudent);
    }

    @PutMapping("/edit")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundedStudent = service.editStudent(student);

        if (foundedStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(foundedStudent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable("id") Long id) {
        service.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-by-age")
    public ResponseEntity<List<Student>> findStudentByAge(@RequestParam int age) {
        return ResponseEntity.ok(service.findByAge(age));
    }

    @GetMapping("/find-by-age-between")
    public ResponseEntity<List<Student>> findStudentListByAgeBetween(@RequestParam("minAge") int min, @RequestParam("maxAge") int max) {
        return ResponseEntity.ok(service.findByAgeBetween(min, max));
    }

    @GetMapping("/get-student-faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok(service.getStudentFaculty(studentId));
    }
}
