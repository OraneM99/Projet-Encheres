package com.eni.eBIDou.categorie;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public class CategorieDAOJpaImpl implements CategorieIDAO{

    private final CategorieRepository repository;

    public CategorieDAOJpaImpl(CategorieRepository repository) {
        this.repository = repository;

    }


    @Override
    public List<Categorie> selectAll() {
        return repository.findAll();
    }

    @Override
    public Categorie selectById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Categorie selectByName(String name) {
        return repository.getCategorieByLibelle(name);
    }

    @Override
    public void ajouterCategorie(Categorie categorie) {
        repository.save(categorie);

    }

    @Override
    public void modifierCategorie(Categorie categorie) {
        repository.save(categorie);

    }

    @Override
    public void supprCategorie(long idCategorie) {
        repository.deleteById(idCategorie);

    }
}
