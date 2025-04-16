package com.eni.eBIDou.retrait;

import com.eni.eBIDou.article.Article;

import java.util.List;

public interface RetraitIDAO {


    List<Retrait> selectAll();

    Retrait selectById(long id);

    Retrait findByArticleId(long id);

    List<Retrait> findByVille(String ville);

    List<Retrait> findByCodePostal(String codePostal);


    void ajouterRetrait(Retrait retrait);


    void supprimerRetrait(long idRetrait);




}
