package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select s from Student s where age=?1")
    List<Student> findByAge(int age);

    @Query("select s from Student s where age >= ?1 and age < ?2")
    List<Student> findByAgeBetween(int min, int max);

    @Query("SELECT f FROM Student AS s, Faculty AS f WHERE s.id = ?1 AND s.faculty.id = f.id")
    Faculty getStudentFaculty(Long studentId);
}
