package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;

import java.util.List;

public interface ArticleIDAO {

    List<Article> selectAll();

    Article selectById(long id);

    List<Article> selectByName(String name);

    List<Article> selectByCategorie(Categorie categorie);

    void ajouterArticle(Article article);

    void updateArticle(Article article);

    void deleteArticle(long idArticle);


}
