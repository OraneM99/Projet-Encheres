package com.eni.eBIDou.initializer;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
public class CategorieInitializer implements DataInitializer {

    private final CategorieRepository categorieRepository;

    public CategorieInitializer(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    @Transactional
    public void initialize() {
        if (isInitialized()) {
            System.out.println("Les catégories sont déjà initialisées");
            return;
        }

        // Création des catégories
        Categorie catInfo = new Categorie();
        catInfo.setLibelle("Informatique");
        catInfo.setArticles(new ArrayList<>());
        categorieRepository.save(catInfo);

        Categorie catAmeub = new Categorie();
        catAmeub.setLibelle("Ameublement");
        catAmeub.setArticles(new ArrayList<>());
        categorieRepository.save(catAmeub);

        Categorie catVet = new Categorie();
        catVet.setLibelle("Vêtement");
        catVet.setArticles(new ArrayList<>());
        categorieRepository.save(catVet);

        Categorie catSport = new Categorie();
        catSport.setLibelle("Sport & Loisirs");
        catSport.setArticles(new ArrayList<>());
        categorieRepository.save(catSport);

        System.out.println("Catégories initialisées avec succès");
    }

    @Override
    public boolean isInitialized() {
        return categorieRepository.count() > 0;
    }

    @Override
    public String getName() {
        return "CategorieInitializer";
    }
}