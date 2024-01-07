package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultySpringBootTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addFacultyTest() throws Exception {
        final String name = "Qwerty";
        final String color = "Red";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);

        String actual = restTemplate.postForObject("http://localhost:" + port + "/faculty/add", faculty, String.class);

        Assertions.assertThat(actual)
                .isNotNull();

        ObjectMapper mapper = new ObjectMapper();
        Faculty actualFaculty = mapper.readValue(actual, Faculty.class);

        Assertions.assertThat(actualFaculty.getName())
                .isNotNull()
                .isEqualTo(faculty.getName());

        Assertions.assertThat(actualFaculty.getColor())
                .isNotNull()
                .isEqualTo(faculty.getColor());

        restTemplate.delete("http://localhost:" + port + "/faculty/remove/" + actualFaculty.getId());
    }

    @Test
    void getFacultyTest() {
        final Long id = 5L;

        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty/get/" + id, String.class))
                .isNotNull();
    }

    @Test
    void editFacultyTest() throws Exception {
        final Long id = 5L;
        final String name = "Qwerty";
        final String color = "Red";

        //Буффертизация старого значения
        String beforeValue = restTemplate.getForObject("http://localhost:" + port + "/faculty/get/" + id, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Faculty beforeFaculty = mapper.readValue(beforeValue, Faculty.class);

        //Подготовка новых данных для изменения
        Faculty newFaculty = new Faculty();
        newFaculty.setId(id);
        newFaculty.setName(name);
        newFaculty.setColor(color);

        //Запрос на изменение данных
        restTemplate.put("http://localhost:" + port + "/faculty/edit", newFaculty);

        //Буфферизация новых значений
        String afterTestValue = restTemplate.getForObject("http://localhost:" + port + "/faculty/get/" + id, String.class);
        mapper = new ObjectMapper();
        Faculty afterFaculty = mapper.readValue(afterTestValue, Faculty.class);

        //Тест
        Assertions.assertThat(afterFaculty.getId())
                .isEqualTo(beforeFaculty.getId())
                .isEqualTo(id);

        Assertions.assertThat(afterFaculty.getName())
                .isNotEqualTo(beforeFaculty.getName())
                .isEqualTo(name);

        Assertions.assertThat(afterFaculty.getColor())
                .isNotEqualTo(beforeFaculty.getColor())
                .isEqualTo(color);

        //Возврат старых значений в БД
        restTemplate.put("http://localhost:" + port + "/faculty/edit", beforeFaculty);
    }

    @Test
    void removeFacultyTest() throws Exception {
        final String name = "Qwerty";
        final String color = "Red";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);

        String testEntry = restTemplate.postForObject("http://localhost:" + port + "/faculty/add", faculty, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Faculty testEntryFaculty = mapper.readValue(testEntry, Faculty.class);


        //Тесты
        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty/get/" + testEntryFaculty.getId(), String.class))
                .isNotNull();

        restTemplate.delete("http://localhost:" + port + "/faculty/remove/" + testEntryFaculty.getId());

        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty/get/" + testEntryFaculty.getId(), String.class))
                .isNull();
    }
}
