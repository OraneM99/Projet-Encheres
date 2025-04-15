package com.eni.eBIDou.article;

import java.util.List;

public interface ArticleIDAO {

    List<Article> selectAll();

    Article selectById(long id);

    Article selectByName(String name);

    //List<ArticleBO> selectByVendeur(UtilisateurBO vendeur);
}
