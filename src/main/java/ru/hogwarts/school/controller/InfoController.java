package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

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

    @GetMapping("/sum")
    public String sum() {
        int limit = 1_000_000;

        StringBuilder stringBuilder = new StringBuilder();

        long timeStart = System.currentTimeMillis();
        int sum = Stream.iterate(1, a -> a + 1)
                .limit(limit)
                .reduce(0, (a, b) -> a + b );
        long timeEnd = System.currentTimeMillis();

        stringBuilder.append(String.format("Calculate %d in %s ms\n", sum, (timeEnd - timeStart)));


        timeStart = System.currentTimeMillis();

        sum = Stream.iterate(1, a -> a + 1)
                .parallel()
                .limit(limit)
                .reduce(0, (a, b) -> a + b);
        timeEnd = System.currentTimeMillis();

        stringBuilder.append(String.format("Optimized calculate %d in %s ms\n", sum, (timeEnd - timeStart)));

        return stringBuilder.toString();
    }
}
