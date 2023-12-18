package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.HogwartsRepository;

import java.util.Set;

@Service
public class HogwartsService {
    private final HogwartsRepository hogwartsRepository;

    public HogwartsService(HogwartsRepository hogwartsRepository) {
        this.hogwartsRepository = hogwartsRepository;
    }

    public int getStudents() {
        return hogwartsRepository.getStudents();
    }

    public double getAvgStudentsAge() {
        return hogwartsRepository.getAvgStudentsAge();
    }

    public Set<Student> getLastFiveStudents() {
        return hogwartsRepository.getLastFiveStudents();
    }
}
