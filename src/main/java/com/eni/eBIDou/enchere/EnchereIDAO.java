package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;

import java.util.List;

public interface EnchereIDAO {

    // non utilisé
    List<Enchere> encheres();

    Enchere findById(long id);

    Enchere findByArticleCible(Article article);

    void nouvelleEnchere(Enchere enchere);

    //non prévu à priori
    void supprimerEnchere(long id);

    void modifierEnchere(Enchere enchere);

    Enchere findEnchereByArticleAndUtilisateur(long articleId, long utilisateurId);
}
