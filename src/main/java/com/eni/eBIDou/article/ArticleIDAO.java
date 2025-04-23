package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.data.EtatVente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ArticleIDAO {

    List<Article> selectAll();
    List<Article> selectByName(String name);
    List<Article> selectByCategorie(Categorie categorie);
    List<Article> selectEncheresEnCours();
    List<Article> selectByNameAndCategorie(String nom, Categorie categorie);
    
    Article selectById(long id);
    Optional<Article> findByEncherisseurId(Long noUtilisateur);

    void ajouterArticle(Article article);
    void updateArticle(Article article);
    void deleteArticle(long idArticle);

    
    // Nouvelles méthodes avec pagination
    Page<Article> selectAllPaginated(Pageable pageable);
    Page<Article> selectByNamePaginated(String name, Pageable pageable);
    Page<Article> selectByCategoriePaginated(Categorie categorie, Pageable pageable);
    Page<Article> selectByNameAndCategoriePaginated(String nom, Categorie categorie, Pageable pageable);
    Page<Article> selectEncheresEnCoursPaginated(EtatVente etatVente, Pageable pageable);
}
