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
        ServiceResponse<Article> articleRecherche = articleService.getArticleById(idArticle);

        Article articleTarget = articleRecherche.data;

        //Recuperer l'utilisateur acteur de l'enchere
        UtilisateurBO acheteurPotent = utilisateurMapper.toBo(utilisateurService.findById(idUtilisateur));

        //Determiner la date du jour
        LocalDateTime aujourdHui = LocalDateTime.now();


        //si la date du jour et avant la date de début d'enchere du produit - l'encher n'est pas accessible
        if (aujourdHui.isBefore(articleTarget.getDateDebutEncheres()) || aujourdHui.isAfter(articleTarget.getDateFinEncheres())) {
            throw new RuntimeException("Enchères non ouvertes pour cet article");
        }

        //Vérifier que l'acheterur potentiel a assez de crédit
        if (acheteurPotent.getCredit() < montant) {
            throw new RuntimeException("Crédit insuffisant");
        }

        //Trouver l'enchere la plus élevé sur l'article ciblé.
        Enchere meilleureEnchere = trouverMeilleureEnchere(articleTarget);


        //montantMin requis pour une nouvelle enchere : si il y a deja un mise : faire + 1 pour la prochaine mise, sinon prend le montant de mise a Prix
        int montantMin = Math.max(
                articleTarget.getMiseAPrix(),
                meilleureEnchere != null ? meilleureEnchere.getMontant_enchere() + 1 : 0);

        // si la somme du montant de l'enchere est trop faible
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
        enchere.setArticleCible(articleTarget);
        enchere.setCrieur(acheteurPotent);
        enchere.setDateEnchere(LocalDateTime.now());
        enchere.setMontant_enchere(montant);

        articleTarget.getEncheres().add(enchere);
        acheteurPotent.getEncheres().add(enchere);

        return enchere;
    }

    private Enchere trouverMeilleureEnchere(Article article) {
        return article.getEncheres().stream()
                .max(Comparator.comparingInt(Enchere::getMontant_enchere))
                .orElse(null);
    }
}



