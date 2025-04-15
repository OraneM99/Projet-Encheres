package com.eni.eBIDou.utilisateurs;

import com.eni.eBIDou.enchere.Enchere;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//@Entity
//@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurBO {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noUtilisateur;

//    @Column(nullable = false, unique = true)
    private String pseudo;

//    @Column(nullable = false)
    private String nom;

//    @Column(nullable = false)
    private String prenom;

//    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;
    private String rue;
    private String codePostal;
    private String ville;

    private int credit;

    private boolean administrateur;
    
    private List<Enchere> encheres;


//  @JsonIgnore
//  @Column(nullable = false)
    private String motDePasse;

    public UtilisateurBO(Long noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, int credit, boolean administrateur, String motDePasse) {
        this.noUtilisateur = noUtilisateur;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.credit = credit;
        this.administrateur = administrateur;
        this.motDePasse = motDePasse;
    }
}
