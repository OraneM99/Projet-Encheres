package com.eni.eBIDou.article;




import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private long noArticle;
    private String nomArticle;
    private String description;

    public Article(long noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, float miseAPrix) {
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

    private Categorie categorieArticle;
    private Retrait lieuRetrait;

}
