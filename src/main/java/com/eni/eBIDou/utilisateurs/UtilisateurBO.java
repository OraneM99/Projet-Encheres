package com.eni.eBIDou.utilisateurs;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noUtilisateur;

    @Column(nullable = false, unique = true)
    private String pseudo;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;
    private String rue;
    private String codePostal;
    private String ville;
    private int credit;
    private boolean administrateur;

    @Column(nullable = false)
    private String motDePasse;
}
