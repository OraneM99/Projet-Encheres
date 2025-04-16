package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public class ArticleDAOJpaImpl implements ArticleIDAO{

    private final ArticleRepository repository;

    public ArticleDAOJpaImpl(ArticleRepository repository) {
        this.repository = repository;
    }



    @Override
    public List<Article> selectAll() {
        return repository.findAll();
    }

    @Override
    public Article selectById(long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<Article> selectByName(String name) {
        return repository.findByNomArticleContainingIgnoreCase(name);
    }

    @Override
    public List<Article> selectByCategorie(Categorie categorie) {
        return repository.findByCategorieArticle(categorie);
    }

    @Override
    public void ajouterArticle(Article article) {
        repository.save(article);

    }

    @Override
    public void updateArticle(Article article) {
        repository.save(article);
    }

    @Override
    public void deleteArticle(long idArticle) {
        repository.deleteById(idArticle);
    }

}
