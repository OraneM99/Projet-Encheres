package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;

import com.eni.eBIDou.data.EtatVente;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class ArticleDAOJpaImpl implements ArticleIDAO {

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
    public List<Article> selectByNameAndCategorie(String name, Categorie categorie) {
        return repository.findByNomArticleContainingIgnoreCaseAndCategorieArticle(name, categorie);
    }

    @Override
    public Optional<Article> findByEncherisseurId(Long noUtilisateur) {
        return repository.findById(noUtilisateur);
    }

    @Override
    public void updateArticle(Article article) {
        repository.save(article);
    }

    @Override
    public void deleteArticle(long idArticle) {
        repository.deleteById(idArticle);
    }

    // Methode pour la pagination
    @Override
    public Page<Article> selectAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Article> selectByNamePaginated(String name, Pageable pageable) {
        return repository.findByNomArticleContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Article> selectByCategoriePaginated(Categorie categorie, Pageable pageable) {
        return repository.findByCategorieArticle(categorie, pageable);
    }

    @Override
    public Page<Article> selectByNameAndCategoriePaginated(String nom, Categorie categorie, Pageable pageable) {
        return repository.findByNomArticleContainingIgnoreCaseAndCategorieArticle(nom, categorie, pageable);
    }

    @Override
    public Page<Article> selectEncheresEnCoursPaginated(EtatVente etatVente, Pageable pageable) {
        return repository.findByEtatVente(etatVente, pageable);
    }

    @Override
    public List<Article> selectEncheresEnCours() {
        return repository.findByEtatVente(EtatVente.EN_COURS);
    }
}
