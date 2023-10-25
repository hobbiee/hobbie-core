package com.br.hobbie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enables the scheduling of tasks
public class HobbieApplication {

    public static void main(String[] args) {
        SpringApplication.run(HobbieApplication.class, args);
    }

}
