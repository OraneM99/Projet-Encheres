package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enchere {
    LocalDateTime dateEnchere;
    int montant_enchere;

    UtilisateurBO crieur;
    Article articleCible;



}
