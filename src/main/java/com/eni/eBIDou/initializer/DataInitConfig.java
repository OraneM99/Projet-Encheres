package com.eni.eBIDou.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DataInitConfig {

    @Bean
    CommandLineRunner initDatabase(DataInitializerService initializerService) {
        return args -> {
            System.out.println("Initialisation de la base de données...");
            initializerService.initializeAll();
            System.out.println("Initialisation de la base de données terminée!");
        };
    }
}