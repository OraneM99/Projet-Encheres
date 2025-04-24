package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurMapper;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.*;

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

    public ServiceResponse<Enchere> getByArticleCible(Article articleCible) {
        Enchere enchere = daoEnchere.findByArticleCible(articleCible);

        if(enchere == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La liste d'enchere pour un article est vide", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, "Liste d'enchere pour relatif à un article recupérée", enchere );
    }

    public ServiceResponse<Enchere> getByArticleAndUtilisateur(long idArticle, long idUtilisateur) {
        Enchere enchere = daoEnchere.findEnchereByArticleAndUtilisateur(idArticle, idUtilisateur);
        if(enchere == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucune enchère n'a pu être récupérée avec cet article et cet utilisateur", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, "Enchère récupérée par son article et son utilisateur", enchere );
    }


    // ############################# CREER UNE ENCHERE ###############################################
    public ServiceResponse<Enchere> placerEnchere(long idArticle, long idUtilisateur, int montant) {

        //Récuperer l'article objet de l'enchere
        ServiceResponse<Article> articleRecherche = articleService.getArticleById(idArticle);
        // Récupérer uniquement les données de l'article
        Article articleTarget = articleRecherche.data;

        //Recuperer l'utilisateur acteur de l'enchere
        UtilisateurBO acheteurPotent = utilisateurMapper.toBo(utilisateurService.findById(idUtilisateur));

        System.out.println("Acheteur potentiel= " + acheteurPotent);

        //Determiner la date du jour
        LocalDate aujourdHui = LocalDate.now();

        //si la date du jour et avant la date de début d'enchere ou apres la date de fin - l'enchere n'est pas accessible
        if (aujourdHui.isBefore(articleTarget.getDateDebutEncheres()) || aujourdHui.isAfter(articleTarget.getDateFinEncheres())) {
            return ServiceResponse.buildResponse(CD_ERR_TCH, "Enchères non ouvertes pour cet article", null);
        }

        //Vérifier que l'acheteur potentiel a assez de crédit
        if (acheteurPotent.getCredit() < montant) {
            return ServiceResponse.buildResponse(CD_ERR_TCH, "L'acheteur n'a pas assez de crédit pour cet article", null);
        }

        //Trouver l'enchère la plus élevée sur l'article ciblé.
        ServiceResponse<Enchere> bestEnchere = trouverMeilleureEnchere(articleTarget.getNoArticle());
        Enchere meilleureEnchere = bestEnchere.data;


        //montantMin requis pour une nouvelle enchere : si il y a deja un mise : faire + 1 pour la prochaine mise, sinon prend le montant de mise a Prix
        int montantMin = Math.max(
                articleTarget.getMiseAPrix(),
                meilleureEnchere != null ? meilleureEnchere.getMontant_enchere() + 1 : 0);

        // si la somme du montant de l'enchere est trop faible
        if (montant < montantMin) {
            return ServiceResponse.buildResponse(CD_ERR_TCH, "Montant trop faible, minimum requis : ", null);
        }

        // Si enchère gagnante précédente, rembourser l'utilisateur
        if (meilleureEnchere != null) {
            UtilisateurBO ancienEncherisseur = meilleureEnchere.getEncherisseur();
            System.out.println("Crédit de l'ancien encherisseur avant= " + ancienEncherisseur.getCredit());
            ancienEncherisseur.setCredit(ancienEncherisseur.getCredit() + meilleureEnchere.getMontant_enchere());
            System.out.println("Crédit de l'ancien encherisseur après= " + ancienEncherisseur.getCredit());

        }

        System.out.println("Crédit de l'acheteur avant= " + acheteurPotent.getCredit());
        System.out.println("Montant misé dans l'enchère= " + montant);

        // Déduire le montant au nouvel encherisseur
        acheteurPotent.setCredit(acheteurPotent.getCredit() - montant);
        //appeler le service pour faire une update du crédit enchérisseur en base.
        utilisateurService.updateCredit(acheteurPotent.getNoUtilisateur(), acheteurPotent.getCredit());
        System.out.println("Crédit de l'acheteur apres= " + acheteurPotent.getCredit());

        //vérifier si une enchère existe deja pour cet utilisateur et cet article
        ServiceResponse<Enchere> updatedEnchere = getByArticleAndUtilisateur(articleTarget.getNoArticle(), acheteurPotent.getNoUtilisateur());
        if(updatedEnchere.getData() != null) {
            // il y a deja une enchere de cet User sut cet article donc mettre à jour.
            updatedEnchere.getData().setMontant_enchere(montant);
            daoEnchere.modifierEnchere(updatedEnchere.getData());
            return ServiceResponse.buildResponse(CD_SUCCESS, "Enchère mise à jour avec le nouveau montant pour cette article", updatedEnchere.getData());
        }

        // si l'utilisateur n'a pas deja fait d'enchere :

        Enchere enchere = new Enchere();
        enchere.setArticleCible(articleTarget);
        enchere.setEncherisseur(acheteurPotent);
        enchere.setDateEnchere(LocalDateTime.now());
        enchere.setMontant_enchere(montant);

        articleTarget.getEncheres().add(enchere);

        daoEnchere.nouvelleEnchere(enchere);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Nouvelle enchère acceptée", enchere) ;
    }



    //######################### TROUVER LA MEILLEURE ENCHERE POUR UN ARTICLE ################
    public ServiceResponse<Enchere> trouverMeilleureEnchere(long idArticle) {
        ServiceResponse<Article> articleRecherche = articleService.getArticleById(idArticle);

        if (articleRecherche.getData().getEncheres().isEmpty()) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucune enchère n'existe sur cet article", null);
        }

        Enchere bestEnchere = articleRecherche.getData().getEncheres().stream().max(Comparator.comparing(Enchere::getMontant_enchere)).get();
        return ServiceResponse.buildResponse(CD_SUCCESS, "Meilleure enchere récupérée pour cet article", bestEnchere);
    }



    //######################### MODIFIIER UNE ENCHERE #########################################
    public ServiceResponse<Enchere> modifierEnchere(Enchere enchere) {
        long idEnchere = enchere.getId();
        Enchere oldEnchere = daoEnchere.findById(idEnchere);
        if (oldEnchere == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucune enchère n'a pu être récupérée pour être modifiée", null);
        }
        daoEnchere.modifierEnchere(enchere);
        return ServiceResponse.buildResponse(CD_SUCCESS, "L'enchère a bien a été mise à jour", null);
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



