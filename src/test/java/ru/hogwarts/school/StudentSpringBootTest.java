package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentSpringBootTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addFacultyTest() throws Exception {
        final Long id = 128L;
        final String name = "Qwerty";
        final int age = 25;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        String actual = restTemplate.postForObject("http://localhost:" + port + "/student/add", student, String.class);

        Assertions.assertThat(actual)
                .isNotNull();

        ObjectMapper mapper = new ObjectMapper();
        Student actualStudent = mapper.readValue(actual, Student.class);

        Assertions.assertThat(actualStudent.getName())
                .isNotNull()
                .isEqualTo(student.getName());

        Assertions.assertThat(actualStudent.getAge())
                .isNotNull()
                .isEqualTo(student.getAge());

        restTemplate.delete("http://localhost:" + port + "/student/remove/" + actualStudent.getId());
    }

    @Test
    void getFacultyTest() {
        final Long id = 5L;

        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/get/" + id, String.class))
                .isNotNull();
    }

    @Test
    void editFacultyTest() throws Exception {
        final Long id = 5L;
        final String name = "Qwerty";
        final int age = 30;

        //Буффертизация старого значения
        String beforeValue = restTemplate.getForObject("http://localhost:" + port + "/student/get/" + id, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Student beforeStudent = mapper.readValue(beforeValue, Student.class);

        //Подготовка новых данных для изменения
        Student newStudent = new Student();
        newStudent.setId(id);
        newStudent.setName(name);
        newStudent.setAge(age);

        //Запрос на изменение данных
        restTemplate.put("http://localhost:" + port + "/student/edit", newStudent);

        //Буфферизация новых значений
        String afterTestValue = restTemplate.getForObject("http://localhost:" + port + "/student/get/" + id, String.class);
        mapper = new ObjectMapper();
        Student afterStudent = mapper.readValue(afterTestValue, Student.class);

        //Тест
        Assertions.assertThat(afterStudent.getId())
                .isEqualTo(beforeStudent.getId())
                .isEqualTo(id);

        Assertions.assertThat(afterStudent.getName())
                .isNotEqualTo(beforeStudent.getName())
                .isEqualTo(name);

        Assertions.assertThat(afterStudent.getAge())
                .isNotEqualTo(beforeStudent.getAge())
                .isEqualTo(age);

        //Возврат старых значений в БД
        restTemplate.put("http://localhost:" + port + "/student/edit", beforeStudent);
    }

    @Test
    void removeFacultyTest() throws Exception {
        final Long id = 128L;
        final String name = "Qwerty";
        final int age = 30;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        String testEntry = restTemplate.postForObject("http://localhost:" + port + "/student/add", student, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Student testEntryStudent = mapper.readValue(testEntry, Student.class);


        //Тесты
        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/get/" + testEntryStudent.getId(), String.class))
                .isNotNull();

        restTemplate.delete("http://localhost:" + port + "/student/remove/" + testEntryStudent.getId());

        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/get/" + testEntryStudent.getId(), String.class))
                .isNull();
    }
}
