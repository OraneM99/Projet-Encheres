package com.eni.eBIDou.article;


import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long noArticle;
    private String nomArticle;
    private String description;

    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEncheres;
    private int miseAPrix;
    private int prixVente;

    @Enumerated(EnumType.STRING)
    private EtatVente etatVente;

    @ManyToOne
    @JoinColumn(name = "utilisateur_noUtilisateur")
    private UtilisateurBO vendeur;

    @ManyToOne
    @JoinColumn(name="categorie_noCategorie")
    private Categorie categorieArticle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "retrait_id")
    private Retrait lieuRetrait;

    @OneToMany(mappedBy = "articleCible")
    private List<Enchere> encheres;

    public Article(long noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, int miseAPrix) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.miseAPrix = miseAPrix;
    }

}
