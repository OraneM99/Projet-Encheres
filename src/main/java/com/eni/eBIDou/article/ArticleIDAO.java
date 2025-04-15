package com.eni.eBIDou.article;

import java.util.List;

public interface ArticleIDAO {

    List<Article> selectAll();

    Article selectById(long id);

    Article selectByName(String name);

    void ajouterArticle(Article article);

    void updateArticle(Article article);

    void deleteArticle(long idArticle);

    //List<ArticleBO> selectByVendeur(UtilisateurBO vendeur);
}
