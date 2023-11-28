package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty addFaculty(Faculty faculty) {
        repository.save(faculty);
        return faculty;
    }

    public Faculty getFacultyById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty newFaculty) {
        if (repository.existsById(newFaculty.getId())) {
            return null;
        }

        repository.save(newFaculty);

        return newFaculty;
    }

    public Faculty removeFaculty(long id) {
        Faculty foundedFaculty = getFacultyById(id);
        repository.delete(foundedFaculty);
        return foundedFaculty;
    }

    public List<Faculty> findByColor(String color) {
        return repository.findByColor(color);
    }
}
