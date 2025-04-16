package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article>findByNomArticleContainingIgnoreCase(String nomArticle);
    List<Article> findByCategorieArticle(Categorie categorieArticle);
}
