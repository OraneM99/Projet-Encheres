package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private long noArticle;
    private String nomArticle;
    private String description;

    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEncheres;
    private int miseAPrix;
    private int prixVente;
    private boolean etatVente;


    private UtilisateurBO vendeur;

    private Categorie categorieArticle;
    private Retrait lieuRetrait;

    private List<Enchere> encheres;

    public Article(long noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, int miseAPrix) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.miseAPrix = miseAPrix;
    }

}
