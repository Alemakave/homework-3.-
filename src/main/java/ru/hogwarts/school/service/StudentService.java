package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(Student student) {
        return repository.save(student);
    }

    public Student getStudent(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Student editStudent(Student newStudent) {
        if (!repository.existsById(newStudent.getId())) {
            return null;
        }

        return repository.save(newStudent);
    }

    public Student removeStudent(Long id) {
        Student foundedStudent = getStudent(id);

        if (foundedStudent == null) {
            return null;
        }

        repository.delete(foundedStudent);

        return foundedStudent;
    }

    public List<Student> findByAge(int age) {
        return repository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return repository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(Long studentId) {
        return repository.getStudentFaculty(studentId);
    }
}
