package com.eni.eBIDou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class EncheresApplication {

    public static void main(String[] args) {
        SpringApplication.run(EncheresApplication.class, args);
    }

}
