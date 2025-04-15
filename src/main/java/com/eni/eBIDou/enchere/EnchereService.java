package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurMapper;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
public class EnchereService {

    private final ArticleService articleService;
    private final UtilisateurService utilisateurService;
    private final UtilisateurMapper utilisateurMapper;

    public EnchereService(ArticleService articleService, UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {
        this.articleService = articleService;
        this.utilisateurService = utilisateurService;
        this.utilisateurMapper = utilisateurMapper;
    }

    public Enchere placerEnchere(Long idArticle, Long idUtilisateur, int montant) {

        //Récuperer l'article objet de l'enchere
        ServiceResponse<Article> articleTarget = articleService.getArticle(idArticle);

        //Recuperer l'utilisateur acteur de l'enchere
        UtilisateurBO acheteurPotent = utilisateurMapper.toBo(utilisateurService.findById(idUtilisateur));

        //Determiner la date du jour
        LocalDateTime aujourdHui = LocalDateTime.now();


        //si la date du jour et avant la date de début d'enchere du produit - l'encher n'est pas accessible
        if (aujourdHui.isBefore(articleTarget.data.getDateDebutEncheres()) || aujourdHui.isAfter(articleTarget.data.getDateFinEncheres())) {
            throw new RuntimeException("Enchères non ouvertes pour cet article");
        }

        //Vérifier que l'acheterur potentiel a assez de crédit
        if (acheteurPotent.getCredit() < montant) {
            throw new RuntimeException("Crédit insuffisant");
        }

        //Trouver l'enchere la plus élevé sur l'article cible.
        Enchere meilleureEnchere = trouverMeilleureEnchere(articleTarget.data);


        int montantMin = Math.max(articleTarget.data.getMiseAPrix(), meilleureEnchere != null ? meilleureEnchere.getMontant_enchere() + 1 : 0);

        if (montant < montantMin) {
            throw new RuntimeException("Montant trop faible, minimum requis : " + montantMin);
        }

        // Si enchère gagnante précédente, rembourser l'utilisateur
        if (meilleureEnchere != null) {
            UtilisateurBO ancienEncherisseur = meilleureEnchere.getCrieur();
            ancienEncherisseur.setCredit(ancienEncherisseur.getCredit() + meilleureEnchere.getMontant_enchere());
        }

        // Déduire le montant au nouvel encherisseur
        acheteurPotent.setCredit(acheteurPotent.getCredit() - montant);

        Enchere enchere = new Enchere();
        enchere.setArticleCible(articleTarget.data);
        enchere.setCrieur(acheteurPotent);
        enchere.setDateEnchere(LocalDateTime.now());
        enchere.setMontant_enchere(montant);

        articleTarget.data.getEncheres().add(enchere);
        acheteurPotent.getEncheres().add(enchere);

        return enchere;
    }

    private Enchere trouverMeilleureEnchere(Article article) {
        return article.getEncheres().stream()
                .max(Comparator.comparingInt(Enchere::getMontant_enchere))
                .orElse(null);
    }
}



