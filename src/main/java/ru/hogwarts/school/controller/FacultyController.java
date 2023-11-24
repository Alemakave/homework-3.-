package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return service.addFaculty(faculty);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable("id") Long id) {
        Faculty foundedFaculty = service.getFacultyById(id);

        if (foundedFaculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(foundedFaculty);
    }

    @PutMapping("/edit")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundedFaculty = service.editFaculty(faculty);

        if (foundedFaculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(foundedFaculty);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeFaculty(@PathVariable("id") Long id) {
        service.removeFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-by-color")
    public ResponseEntity<List<Faculty>> findFacultyByColor(@RequestParam String name, @RequestParam String color) {
        if ((name != null && !name.isBlank())
                || (color != null && !color.isBlank())) {
            return ResponseEntity.ok(service.findByColor(name, color));
        }

        return ResponseEntity.ok(List.of());
    }
}
