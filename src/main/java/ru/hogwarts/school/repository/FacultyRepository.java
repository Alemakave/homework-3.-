package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("SELECT f FROM Faculty f WHERE ((UPPER(name) line UPPER(?1)) OR (UPPER(color) like UPPER(?2)))")
    List<Faculty> findByColor(String name, String color);
}
