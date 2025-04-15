package com.eni.eBIDou.article;

import java.util.List;

public interface ArticleService {

    List<ArticleBO> selectAll();

    ArticleBO selectById(long id);

    ArticleBO selectByName(String name);

    //List<ArticleBO> selectByVendeur(UtilisateurBO vendeur);
}
