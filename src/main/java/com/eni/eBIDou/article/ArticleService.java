package com.eni.eBIDou.article;

import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.ResourceTransactionManager;

import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.CD_ERR_NOT_FOUND;
import static com.eni.eBIDou.service.ServiceConstant.CD_SUCCESS;

@Service
public class ArticleService {

    private final ResourceTransactionManager resourceTransactionManager;
    private ArticleIDAO daoArticle;

    public ArticleService(ArticleIDAO daoArticle, ResourceTransactionManager resourceTransactionManager) {
        this.daoArticle = daoArticle;
        this.resourceTransactionManager = resourceTransactionManager;
    }

    public ServiceResponse<List<Article>> getAll(){
        List<Article> listesArticles = daoArticle.selectAll();

        // Erreur : 111 (Liste vide)
        if (listesArticles.isEmpty()){

            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Liste vide et invalide", listesArticles);
        }
        // Success : 200
        return ServiceResponse.buildResponse(CD_SUCCESS, "La liste des personnes a été récupérée avec succès", listesArticles);
    }


    public ServiceResponse<Article> getArticle(long id){
        Article articheCherche = daoArticle.selectById(id);

        //Erreur : 111 (Article non trouvé)
        if(articheCherche == null){
            return  ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Article non trouvé par son id", articheCherche);
        }

        // Success : 200
        return ServiceResponse.buildResponse(CD_SUCCESS, "Article trouvé", articheCherche);
    }


    public ServiceResponse<Article> getArticleByName(String name){
        Article articheCherche = daoArticle.selectByName(name);

        //Erreur : 111( Article non trouvé)
        if(articheCherche == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Article non trouvé par son nom", articheCherche);
        }

        return ServiceResponse.buildResponse(CD_SUCCESS, "Article récupéré par son nom", articheCherche);
    }



    public ServiceResponse<Article> addArticle(Article article) {
        daoArticle.ajouterArticle(article);

        return ServiceResponse.buildResponse(CD_SUCCESS, "Article créé avec succes",article );
    }

    public ServiceResponse<Article> updateArticle(Article article) {

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
