package com.eni.eBIDou.article;




import com.eni.eBIDou.categorie.CategorieBO;
import com.eni.eBIDou.retrait.RetraitBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBO {
    private long noArticle;
    private String nomArticle;
    private String description;

    public ArticleBO(long noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, float miseAPrix) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.miseAPrix = miseAPrix;
    }

    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEncheres;
    private float miseAPrix;
    private float prixVente;
    private boolean etatVente;


    private UtilisateurBO vendeur;
    private UtilisateurBO acheteur;
    private CategorieBO categorieArticle;
    private RetraitBO lieuRetrait;

}
