package com.eni.eBIDou.initializer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Profile("dev")
public class DataInitializerService {

    private final List<DataInitializer> initializers;

    public DataInitializerService(List<DataInitializer> initializers) {
        this.initializers = initializers;
    }

    public void initializeAll() {
        initializers.forEach(initializer -> {
            System.out.println("Exécution de l'initializer: " + initializer.getName());
            initializer.initialize();
        });
    }

    public void initializeSpecific(String initializerName) {
        initializers.stream()
                .filter(initializer -> initializer.getName().equals(initializerName))
                .findFirst()
                .ifPresentOrElse(
                        initializer -> {
                            System.out.println("Exécution de l'initializer spécifique: " + initializer.getName());
                            initializer.initialize();
                        },
                        () -> System.out.println("Initializer non trouvé: " + initializerName)
                );
    }

    public void initializeEncheres() {
        initializeSpecific("EnchereInitializer");
    }
}