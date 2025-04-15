package com.eni.eBIDou.article;

import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.CD_ERR_NOT_FOUND;
import static com.eni.eBIDou.service.ServiceConstant.CD_SUCCESS;

@Service
public class ArticleService {
    @Autowired
    ArticleIDAO daoArticle;

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
            return  ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Article non trouvé", articheCherche);
        }

        // Success : 200
        return ServiceResponse.buildResponse(CD_SUCCESS, "Article trouvé", articheCherche);

    }

}
