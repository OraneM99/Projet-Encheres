package com.eni.eBIDou.utilisateurs;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noUtilisateur;

    @NotBlank
    @Column(unique = true)
    private String pseudo;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String telephone;

    @NotBlank
    private String rue;

    @NotBlank
    private String codePostal;

    @NotBlank
    private String ville;

    @NotBlank
    private String motDePasse;

    @Min(0)
    private int credit = 0;

    private boolean administrateur = false;
}
