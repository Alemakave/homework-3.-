package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @Value("${server.port:8080}")
    private int port;

    @GetMapping("/port")
    public String getPort() {
        return String.format(
                "{\n" +
                "\t\"port\":%d\n" +
                "}", port);
    }
}
