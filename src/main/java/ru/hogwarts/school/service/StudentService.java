package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final HashMap<Long, Student> registeredStudents = new HashMap<>();

    public Student addStudent(Student student) {
        registeredStudents.put(student.getId(), student);

        return student;
    }

    public Student getStudent(Long id) {
        return registeredStudents.get(id);
    }

    public Student editStudent(Student newStudent) {
        if (!registeredStudents.containsKey(newStudent.getId())) {
            return null;
        }

        registeredStudents.put(newStudent.getId(), newStudent);
        return newStudent;
    }

    public Student removeStudent(Long id) {
        return registeredStudents.remove(id);
    }

    public List<Student> findByAge(int age) {
        return registeredStudents.values().stream().filter(student -> student.getAge() == age).collect(Collectors.toList());
    }
}
