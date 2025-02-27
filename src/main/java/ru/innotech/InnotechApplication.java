package ru.innotech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class InnotechApplication {
    public static void main(String[] args) {
        SpringApplication.run(InnotechApplication.class, args);
    }
}
