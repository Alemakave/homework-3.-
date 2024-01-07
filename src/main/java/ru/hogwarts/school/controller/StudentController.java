package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService service;
    private final AvatarService avatarService;

    public StudentController(StudentService service, AvatarService avatarService) {
        this.service = service;
        this.avatarService = avatarService;
    }

    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
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

    @DeleteMapping("/remove/{id}")
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

    @GetMapping("/{id}/get-avatar")
    public void getAvatarFromFile(@PathVariable("id") Long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.getAvatar(studentId);
        try (InputStream is = Files.newInputStream(Path.of(avatar.getFilePath()));
             OutputStream os = response.getOutputStream()) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(avatar.getMediaType());
            response.setContentLengthLong(avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/{id}/get-avatar/preview")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Long studentId) {
        Avatar avatar = avatarService.getAvatar(studentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @PostMapping(value = "/{id}/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable("id") Long studentId, @RequestParam MultipartFile avatar) {
        if (avatar.getSize() > 300 * 1024) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is to big!");
        }
        try {
            avatarService.uploadAvatar(studentId, avatar);
        } catch (IOException e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all-students-sorted")
    public ResponseEntity<List<Student>> getAllStudentsSorted() {
        return ResponseEntity.ok(service.getAllStudentsSorted());
    }

    @GetMapping("/get-all-students-avg-age")
    public ResponseEntity<Double> getAllStudentsAvgAge() {
        return ResponseEntity.ok(service.getAllStudentsAvgAge());
    }
}
