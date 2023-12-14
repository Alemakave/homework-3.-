package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Set;

public interface HogwartsRepository extends JpaRepository<Student, Long> {
    @Query("SELECT count(s) FROM Student s")
    int getStudents();

    @Query("SELECT avg(s.age) FROM Student s")
    int getAvgStudentsAge();

    @Query(value = "SELECT * FROM Student AS s ORDER BY s.id DESC LIMIT 5", nativeQuery = true)
    Set<Student> getLastFiveStudents();
}
