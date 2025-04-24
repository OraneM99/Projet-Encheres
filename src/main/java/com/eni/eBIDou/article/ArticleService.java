package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.eni.eBIDou.service.ServiceConstant.*;

@Service
public class ArticleService {

    private final ArticleIDAO daoArticle;
    private final ArticleRepository articleRepository;
    private final EnchereService enchereService;
    private final UtilisateurService utilisateurService;

    public ArticleService(ArticleIDAO daoArticle, ArticleRepository articleRepository, EnchereService enchereService, UtilisateurService utilisateurService) {
        this.daoArticle = daoArticle;
        this.articleRepository = articleRepository;
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
    }

    @Transactional
    public ServiceResponse<List<Article>> verifierEtMettreAjourEtatVente() {
        //Recuperer la date du jour
        LocalDate today = LocalDate.now();
        //récupérer tous les articles
        List<Article> articles = articleRepository.findAll();
        if(articles.isEmpty()) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucun article récupéré pour être mis à jour", null);
        }

        //Pour chaque article
        for (Article article : articles) {
            //on récupère l'état
            EtatVente etat = article.getEtatVente();


            if (etat == EtatVente.CREEE && !today.isBefore(article.getDateDebutEncheres())) {
                article.setEtatVente(EtatVente.EN_COURS);
            }
            if (etat == EtatVente.EN_COURS && today.isAfter(article.getDateFinEncheres())) {
                article.setEtatVente(EtatVente.TERMINEE);
            }

        }
        articleRepository.saveAll(articles);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Les articles ont bien été mis à jour", articles);
    }

    @Transactional
    public  ServiceResponse<Article>marquerRetraitEffectue(Long articleId) {

        Article article = daoArticle.selectById(articleId);

        if (article.getEtatVente() != EtatVente.TERMINEE) {
            return ServiceResponse.buildResponse(CD_ERR_TCH, "Le retrait ne peut être confirmé que pour une enchère terminée.", null );
        }
        // récupérer l'enchere gagnante de l'article
        ServiceResponse<Enchere> EnchereResponse = enchereService.trouverMeilleureEnchere(articleId);
        Enchere meilleureEnchere = EnchereResponse.getData();
        int montant = meilleureEnchere.getMontant_enchere();

        //recuperer le vendeur pour lui virer la somme
        UtilisateurBO vendeur = article.getVendeur();
        utilisateurService.updateCredit(vendeur.getNoUtilisateur(), montant);


        article.setEtatVente(EtatVente.RETRAIT_EFFECTUE);
        articleRepository.save(article);
        return ServiceResponse.buildResponse(CD_SUCCESS, "L'etat vente de l'article a bien été mis à jour par le retrait effectué", article);
    }


    //######################################## METHODE DE RECUPERATION #################################

    // Récupère tous les articles
    public ServiceResponse<List<Article>> getAll() {
        return getArticlesResponse(daoArticle.selectAll(), "Liste vide et invalide", "La liste des articles a été récupérée avec succès");
    }

    // Récupère un article par son ID
    public ServiceResponse<Article> getArticleById(long id) {
        return getArticleResponse(daoArticle.selectById(id), "Article non trouvé par son id", "Article trouvé");
    }

    // Récupère un article par son nom
    public ServiceResponse<List<Article>> getArticlesByName(String name) {
        return getArticlesResponse(daoArticle.selectByName(name), "Article non trouvé par son nom", "Article récupéré par son nom");
    }

    // Récupère les articles par catégorie
    public ServiceResponse<List<Article>> getArticlesByCategorie(Categorie categorie) {
        return getArticlesResponse(daoArticle.selectByCategorie(categorie), "Aucun article pour cette catégorie", "Liste d'article par catégorie récupérée avec succès");
    }

    // Récupère les enchères en cours
    public ServiceResponse<List<Article>> getAllEncheresEnCours() {
        return getArticlesResponse(daoArticle.selectEncheresEnCours(), "Aucune enchère en cours trouvée", "Liste des enchères en cours récupérée avec succès");
    }

    // Recherche des articles selon un critère (nom ou catégorie)
    public ServiceResponse<List<Article>> rechercherArticles(String search, Categorie categorie) {
        List<Article> articles = getArticlesBySearchAndCategory(search, categorie);
        return getArticlesResponse(articles, "Aucun article trouvé selon les critères", "Articles récupérés avec succès");
    }

    // Recherche des articles pour un enchérisseur spécifique
    public ServiceResponse<Article> findByEncherisseurId(Long noUtilisateur) {
        Optional<Article> articleOptional = daoArticle.findByEncherisseurId(noUtilisateur);

        return articleOptional.map(article -> ServiceResponse.buildResponse(CD_SUCCESS, "Article récupéré avec succès", article)).orElseGet(() -> ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucune enchère trouvée.", null));
        
    }


    //###################################### METHODE CRUD ##############################################

    // Ajoute un nouvel article
    public ServiceResponse<Article> addArticle(Article article) {
        article.setEtatVente(EtatVente.CREEE);
        daoArticle.ajouterArticle(article);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Article créé avec succès", article);
    }

    // Met à jour un article existant
    public ServiceResponse<Article> updateArticle(Article article) {
        Article articleAModifier = daoArticle.selectById(article.getNoArticle());

        if (articleAModifier == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La modification de l'article n'a pas été réalisée, il n'a pas été récupéré", null);
        }

        daoArticle.updateArticle(article);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Article mis à jour avec succès", article);
    }

    // Supprime un article par son ID
    public ServiceResponse<Article> deleteArticle(long id) {
        Article articleToDelete = daoArticle.selectById(id);

        if (articleToDelete == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "L'article à supprimer n'existe pas", null);
        }

        daoArticle.deleteArticle(id);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Article supprimé avec succès", null);
    }

    // Méthode utilitaire pour gérer les réponses d'articles (liste)
    private ServiceResponse<List<Article>> getArticlesResponse(List<Article> articles, String errorMessage, String successMessage) {
        if (articles == null || articles.isEmpty()) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, errorMessage, articles);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, successMessage, articles);
    }

    // Méthode utilitaire pour gérer les réponses d'articles (individuel)
    private ServiceResponse<Article> getArticleResponse(Article article, String errorMessage, String successMessage) {
        if (article == null) {
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, errorMessage, article);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, successMessage, article);
    }

    // Méthode utilitaire pour centraliser la logique de recherche des articles (nom et catégorie)
    private List<Article> getArticlesBySearchAndCategory(String search, Categorie categorie) {
        if ((search == null || search.isBlank()) && categorie == null) {
            return daoArticle.selectAll();
        } else if (categorie != null && (search == null || search.isBlank())) {
            return daoArticle.selectByCategorie(categorie);
        } else if (categorie == null) {
            return daoArticle.selectByName(search);
        } else {
            return daoArticle.selectByNameAndCategorie(search, categorie);
        }
    }
}
