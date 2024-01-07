package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository repository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for add faculty");
        return repository.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        logger.info("Was invoked method for get faculty by id");
        return repository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty newFaculty) {
        logger.info("Was invoked method for edit faculty");
        if (!repository.existsById(newFaculty.getId())) {
            logger.error("Faculty not found by id=" + newFaculty.getId());
            return null;
        }

        return repository.save(newFaculty);
    }

    public Faculty removeFaculty(long id) {
        logger.info("Was invoked method for remove faculty");

        Faculty foundedFaculty = getFacultyById(id);
        if (foundedFaculty == null) {
            logger.error("Not found student by id=" + id);
            return null;
        }
        repository.delete(foundedFaculty);

        return foundedFaculty;
    }

    public List<Faculty> findByColor(String name, String color) {
        logger.info("Was invoked method for find by color");
        return repository.findByColor(name, color);
    }

    public Set<Student> findStudentsByFaculty(Long facultyId) {
        logger.info("Was invoked method for find students by faculty");
        return repository.findStudentsByFaculty(facultyId);
    }

    public String getLongestFacultyName() {
        List<Faculty> faculties = repository.findAll();

        return faculties.stream()
                .sorted(Comparator.comparingInt(o -> ((Faculty)o).getName().length()).reversed())
                .collect(Collectors.toList())
                .get(0)
                .getName();
    }
}
