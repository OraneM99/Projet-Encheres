package com.eni.eBIDou.categorie;

import java.util.List;

public interface CategorieIDAO {

    List<Categorie> selectAll();

    Categorie selectById(long id);

    Categorie selectByName(String name);

    void ajouterCategorie(Categorie categorie);

    void modifierCategorie(Categorie categorie);

    void supprCategorie(long idCategorie);

}
