package com.eni.encheres.article;




import com.eni.encheres.categorie.CategorieBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBO {
    private long noArticle;
    private String nomArticle;
    private String description;
    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEncheres;
    private float miseAPrix;
    private float prixVente;
    private boolean etatVente;

    private UtilisateurBO vendeur;
    private UtilisateurBO acheteur;
    private CategorieBO categorieArticle;
    private Retrait lieuRetrait;

}
