package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final HashMap<Long, Faculty> registeredFaculties = new HashMap<>();

    public Faculty addFaculty(Faculty faculty) {
        registeredFaculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFacultyById(Long id) {
        return registeredFaculties.get(id);
    }

    public Faculty editFaculty(Faculty newFaculty) {
        if (!registeredFaculties.containsKey(newFaculty.getId())) {
            return null;
        }

        registeredFaculties.put(newFaculty.getId(), newFaculty);
        return newFaculty;
    }

    public Faculty removeFaculty(long id) {
        return registeredFaculties.remove(id);
    }

    public List<Faculty> findByColor(String color) {
        return registeredFaculties.values().stream().filter(faculty -> faculty.getColor().equals(color)).collect(Collectors.toList());
    }
}
