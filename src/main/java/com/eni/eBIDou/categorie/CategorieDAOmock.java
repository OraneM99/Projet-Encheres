package com.eni.eBIDou.categorie;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("mock")
public class CategorieDAOmock implements CategorieIDAO{

    List<Categorie> categorieList = new ArrayList<>();

    public CategorieDAOmock() {
        categorieList.add(new Categorie(1l, "Maison"));
        categorieList.add(new Categorie(2l, "Jardin"));
        categorieList.add(new Categorie(3l, "Papetterie"));
        categorieList.add(new Categorie(4l, "Boisson"));
        categorieList.add(new Categorie(5l, "Voiture"));
        categorieList.add(new Categorie(6l, "Vetement"));
        categorieList.add(new Categorie(7l, "Informatique"));
        categorieList.add(new Categorie(8l, "Autre"));

    }


    @Override
    public List<Categorie> selectAll() {
        return categorieList;
    }

    @Override
    public Categorie selectById(long id) {
        for (Categorie categorie : categorieList) {
            if(categorie.getNoCategorie() == id) {
                return categorie;
            }
        }
        return null;
    }

    @Override
    public Categorie selectByName(String name) {
        for (Categorie categorie : categorieList) {
            if(categorie.getLibelle().equals(name)) {
                return categorie;
            }
        }
        return null;
    }


    @Override
    public void ajouterCategorie(Categorie newCategorie) {
        if(!categorieList.contains(newCategorie)) {
            categorieList.add(newCategorie);

        }

    }

    @Override
    public void modifierCategorie(Categorie newCategorie) {
        for(Categorie categorie : categorieList) {
            if(categorie.getNoCategorie() == newCategorie.getNoCategorie()) {
                categorieList.remove(categorie);
                categorieList.add(newCategorie);
            }
        }

    }

    @Override
    public void supprCategorie(long idCategorie) {
        for(Categorie categorie : categorieList) {
            if(categorie.getNoCategorie() == idCategorie) {
                categorieList.remove(categorie);
            }
        }
    }
}
