package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;

import java.util.List;

public interface EnchereIDAO {

    List<Enchere> encheres();

    Enchere findById(long id);

    Enchere findByArticleCible(Article article);

    void nouvelleEnchere(Enchere enchere);

    void supprimerEnchere(long id);
}
