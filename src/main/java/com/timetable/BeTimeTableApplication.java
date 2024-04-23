package com.timetable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeTimeTableApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeTimeTableApplication.class, args);
    }

}
