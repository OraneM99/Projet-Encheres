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
import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.CD_ERR_NOT_FOUND;
import static com.eni.eBIDou.service.ServiceConstant.CD_SUCCESS;

@Service
public class EnchereService {

    private EnchereIDAO daoEnchere;
    private final ArticleService articleService;
    private final UtilisateurService utilisateurService;
    private final UtilisateurMapper utilisateurMapper;


    public EnchereService(EnchereIDAO daoEnchere ,ArticleService articleService, UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {
        this.daoEnchere = daoEnchere;
        this.articleService = articleService;
        this.utilisateurService = utilisateurService;
        this.utilisateurMapper = utilisateurMapper;
    }

    public ServiceResponse<List<Enchere>> getAll() {
        List<Enchere> listeEncheres = daoEnchere.encheres();
        //Erreur 111
        if(listeEncheres.isEmpty()) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La liste est vide", listeEncheres);
        }

        //succes 200
        return ServiceResponse.buildResponse(CD_SUCCESS, "Liste enchere récupérèes", listeEncheres);
    }

    public ServiceResponse<Enchere> getById(long id) {
        Enchere enchere = daoEnchere.findById(id);

        if(enchere == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucune enchere ne correspond à cet id", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, "Enchere récupérée", enchere);
    }

    public ServiceResponse<List<Enchere>> getByArticleCible(Article articleCible) {
        List<Enchere> listeEncheres = daoEnchere.findByArticleCible(articleCible);

        if(listeEncheres.isEmpty()) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La liste d'enchere pour un article est vide", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, "Liste d'enchere pour relatif à un article recupérée", listeEncheres);
    }


    // ############################# CREER UNE ENCHERE ###############################################
    public ServiceResponse<Enchere> placerEnchere(Long idArticle, Long idUtilisateur, int montant) {

        //Récuperer l'article objet de l'enchere
        ServiceResponse<Article> articleRecherche = articleService.getArticleById(idArticle);
        // Récupérer uniquement les données de l'article (sans message et code)
        Article articleTarget = articleRecherche.data;

        //Recuperer l'utilisateur acteur de l'enchere
        UtilisateurBO acheteurPotent = utilisateurMapper.toBo(utilisateurService.findById(idUtilisateur));

        //Determiner la date du jour
        LocalDateTime aujourdHui = LocalDateTime.now();

        //si la date du jour et avant la date de début d'enchere du produit - l'enchere n'est pas accessible
        if (aujourdHui.isBefore(articleTarget.getDateDebutEncheres()) || aujourdHui.isAfter(articleTarget.getDateFinEncheres())) {
            throw new RuntimeException("Enchères non ouvertes pour cet article");
        }

        //Vérifier que l'acheteur potentiel a assez de crédit
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
            UtilisateurBO ancienEncherisseur = meilleureEnchere.getEncherisseur();
            ancienEncherisseur.setCredit(ancienEncherisseur.getCredit() + meilleureEnchere.getMontant_enchere());
        }

        // Déduire le montant au nouvel encherisseur
        acheteurPotent.setCredit(acheteurPotent.getCredit() - montant);

        Enchere enchere = new Enchere();
        enchere.setArticleCible(articleTarget);
        enchere.setEncherisseur(acheteurPotent);
        enchere.setDateEnchere(LocalDateTime.now());
        enchere.setMontant_enchere(montant);

        articleTarget.getEncheres().add(enchere);

        daoEnchere.nouvelleEnchere(enchere);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Nouvelle enchere acceptée", enchere) ;
    }

    //######################### TROUVER LA MEILLEURE ENCHERE POUR UN ARTICLE ################
    private Enchere trouverMeilleureEnchere(Article article) {
        return article.getEncheres().stream()
                .max(Comparator.comparingInt(Enchere::getMontant_enchere))
                .orElse(null);
    }


    //########################## SUPPRIMER UNE ENCHERE  ########################################

    public ServiceResponse<Enchere> supprimerEnchere(long idEnchere) {
        Enchere enchere = daoEnchere.findById(idEnchere);
        if(enchere == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Suppression d'enchere impossible, elle n'a pas été trouvée", null);
        }

        daoEnchere.supprimerEnchere(idEnchere);
        return ServiceResponse.buildResponse(CD_SUCCESS,"Suppression de l'enchère effectuée avec succes", enchere);
    }

}



