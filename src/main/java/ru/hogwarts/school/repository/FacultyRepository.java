package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Set;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("SELECT f FROM Faculty f WHERE ((UPPER(name) LIKE UPPER(?1)) OR (UPPER(color) LIKE UPPER(?2)))")
    List<Faculty> findByColor(String name, String color);

    @Query("SELECT s FROM Faculty as f, Student as s WHERE f.id = ?1 and f.id = s.faculty.id")
    Set<Student> findStudentsByFaculty(Long facultyId);

//    @Query("SELECT s FROM Faculty f, Student s WHERE f.id = ?1 AND f.id = s.faculty_id")
//    List<Student> findStudentsFromFaculty(Long facultyId);
}
