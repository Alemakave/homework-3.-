package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Integer count = 0;

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

    public List<Student> getAllStudentsSorted() {
        List<Student> students = repository.findAll();

        return students.stream()
                .parallel()
                .peek(student -> student.setName(student.getName().toUpperCase()))
                .filter(student -> student.getName().startsWith("A"))
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    public double getAllStudentsAvgAge() {
        List<Student> students = repository.findAll();

        int allStudentsAge = students
                .stream()
                .map(Student::getAge)
                .parallel()
                .reduce(Integer::sum)
                .orElseThrow();

        return (double) allStudentsAge / students.size();
    }

    public void printParallel() {
        List<Student> students = repository.findAll()
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        System.out.println("MT(0): " + students.get(0).getName());
        System.out.println("MT(1): " + students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println("T1(2): " + students.get(2).getName());
            System.out.println("T1(3): " + students.get(3).getName());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("T2(4): " + students.get(4).getName());
            System.out.println("T2(5): " + students.get(5).getName());
        });

        thread1.start();
        thread2.start();
    }

    public void printSynchronized() {
        List<Student> students = repository.findAll()
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        System.out.println("MT(0): " + students.get(0).getName());
        System.out.println("MT(1): " + students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            printSync("T1(2): " + students.get(2).getName());
            printSync("T1(3): " + students.get(3).getName());
        });
        Thread thread2 = new Thread(() -> {
            printSync("T2(4): " + students.get(4).getName());
            printSync("T2(5): " + students.get(5).getName());
        });

        thread1.start();
        thread2.start();
    }

    public void printSynchronizedUseSyncBlock() {
        List<Student> students = repository.findAll()
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        count = 0;
        System.out.println("MT(" + count + "): " + students.get(count).getName());
        count++;
        System.out.println("MT(" + count + "): " + students.get(count).getName());
        count++;

        Thread thread1 = new Thread(() -> {
            synchronized (count) {
                System.out.println("T1(" + count + "): " + students.get(count).getName());
                count++;
                System.out.println("T1(" + count + "): " + students.get(count).getName());
                count++;
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (count) {
                System.out.println("T2(" + count + "): " + students.get(count).getName());
                count++;
                System.out.println("T2(" + count + "): " + students.get(count).getName());
            }
        });

        thread1.start();
        thread2.start();
    }

    private synchronized void printSync(String message) {
        System.out.println(message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
