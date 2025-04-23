package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;

import com.eni.eBIDou.data.EtatVente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByEtatVente(EtatVente etatVente);

    List<Article> findByNomArticleContainingIgnoreCase(String nomArticle);

    List<Article> findByNomArticleContainingIgnoreCaseAndCategorieArticle(String nomArticle, Categorie categorie);

    List<Article> findByCategorieArticle(Categorie categorie);
}
