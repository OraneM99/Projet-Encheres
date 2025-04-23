package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;

import java.util.List;
import java.util.Optional;

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
