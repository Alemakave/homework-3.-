package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.HogwartsRepository;

import java.util.Set;

@Service
public class HogwartsService {
    private final HogwartsRepository hogwartsRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HogwartsService(HogwartsRepository hogwartsRepository) {
        this.hogwartsRepository = hogwartsRepository;
    }

    public int getStudents() {
        logger.info("Was invoked method for get school students");
        return hogwartsRepository.getStudents();
    }

    public double getAvgStudentsAge() {
        logger.info("Was invoked method for get average students age");
        return hogwartsRepository.getAvgStudentsAge();
    }

    public Set<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last 5 students");
        return hogwartsRepository.getLastFiveStudents();
    }
}
