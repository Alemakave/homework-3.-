package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for add student");
        return repository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for get student by id");
        return repository.findById(id).orElse(null);
    }

    public Student editStudent(Student newStudent) {
        logger.info("Was invoked method for edit student");
        if (!repository.existsById(newStudent.getId())) {
            logger.error("Not found student by id=" + newStudent.getId());
            return null;
        }

        return repository.save(newStudent);
    }

    public Student removeStudent(Long id) {
        logger.info("Was invoked method for remove student");
        Student foundedStudent = getStudent(id);

        if (foundedStudent == null) {
            logger.error("Not found student by id=" + id);
            return null;
        }

        repository.delete(foundedStudent);

        return foundedStudent;
    }

    public List<Student> findByAge(int age) {
        logger.info("Was invoked method for find students by age");
        return repository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students between age");
        return repository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(Long studentId) {
        logger.info("Was invoked method for get student faculty");
        return repository.getStudentFaculty(studentId);
    }
}
