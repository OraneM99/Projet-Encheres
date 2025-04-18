package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.*;

@Service
public class ArticleService {
    
    private ArticleIDAO daoArticle;

    public ArticleService(ArticleIDAO daoArticle) {
        this.daoArticle = daoArticle;
    }



    // ############################       GET ALL #####################################
    public ServiceResponse<List<Article>> getAll(){
        List<Article> listesArticles = daoArticle.selectAll();

        // Erreur : 111 (Liste vide)
        if (listesArticles.isEmpty()){

            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Liste vide et invalide", listesArticles);
        }
        // Success : 200
        return ServiceResponse.buildResponse(CD_SUCCESS, "La liste des articles a été récupérée avec succès", listesArticles);
    }


    public ServiceResponse<Article> getArticleById(long id){
        Article articheCherche = daoArticle.selectById(id);

        //Erreur : 111 (Article non trouvé)
        if (articheCherche == null){
            return  ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Article non trouvé par son id", articheCherche);
        }

        // Success : 200
        return ServiceResponse.buildResponse(CD_SUCCESS, "Article trouvé", articheCherche);
    }


    public ServiceResponse<List<Article>> getArticlesByName(String name){
        List<Article> artichesCherches = daoArticle.selectByName(name);

        //Erreur : 111( Article non trouvé)
        if(artichesCherches == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Article non trouvé par son nom", artichesCherches);
        }

        return ServiceResponse.buildResponse(CD_SUCCESS, "Article récupéré par son nom", artichesCherches);
    }

    public ServiceResponse<List<Article>> getArticlesByCategorie(Categorie categorie){
        List<Article>articlesByCategorie = daoArticle.selectByCategorie(categorie);

        if(articlesByCategorie == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucun article pour cette catégorie", null);
        }

        return ServiceResponse.buildResponse(CD_SUCCESS, "Liste d'article par catégorie récupérée avec succès", articlesByCategorie);
    }



    public ServiceResponse<Article> addArticle(Article article) {
        article.setEtatVente(EtatVente.CREEE);
        daoArticle.ajouterArticle(article);

        return ServiceResponse.buildResponse(CD_SUCCESS, "Article créé avec succes",article );
    }

    public ServiceResponse<Article> updateArticle(Article article) {
        Article articleAModifier = daoArticle.selectById(article.getNoArticle());

        if (articleAModifier == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La modification de l'article n'a pas été réalisé, il n'a pas été recupéré", null);
        }

        daoArticle.updateArticle(article);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Article mis à jour avec succes",article );

    }

    public ServiceResponse<Article> deleteArticle(long id) {

        Article articleToDelete = daoArticle.selectById(id);
        if(articleToDelete == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "L'article à supprimer n'existe pas",null);
        }
        daoArticle.deleteArticle(id);

        return ServiceResponse.buildResponse(CD_SUCCESS, "Article supprimé avec succes", null);

    }

}
