package com.eni.eBIDou.enchere;


import com.eni.eBIDou.article.Article;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public class EnchereDAOJpaImpl implements EnchereIDAO{

    private final EnchereRepository repository;

    public EnchereDAOJpaImpl(EnchereRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Enchere> encheres() {
        return repository.findAll();
    }

    @Override
    public Enchere findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Enchere findByArticleCible(Article article) {
        return repository.findByArticleCible(article);
    }

    @Override
    public void nouvelleEnchere(Enchere enchere) {
        repository.save(enchere);

    }

    @Override
    public void supprimerEnchere(long id) {
        repository.deleteById(id);

    }

    @Override
    public void modifierEnchere(Enchere enchere) {
        repository.save(enchere);
    }

    @Override
    public Enchere findEnchereByArticleAndUtilisateur(long articleId, long utilisateurId) {
        return repository.findByArticleCible_NoArticleAndEncherisseur_NoUtilisateur(articleId, utilisateurId);
    }
}
