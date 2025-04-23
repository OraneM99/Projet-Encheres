package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnchereRepository extends JpaRepository<Enchere, Long> {

    Enchere findByArticleCible(Article articleCible);


    Enchere findByArticleCible_NoArticleAndEncherisseur_NoUtilisateur (long articleNoArticle, long utilisateurNoUtilisateur);
}
