package com.eni.eBIDou.retrait;


import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.*;

@Service
public class RetraitService {

    private final RetraitIDAO daoRetrait;

    public RetraitService(RetraitIDAO daoRetrait) {
        this.daoRetrait = daoRetrait;
    }

    public ServiceResponse<List<Retrait>> getAll(){
        List<Retrait> listesRetraits = daoRetrait.selectAll();

        if (listesRetraits.isEmpty()){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Liste de retrait vide", null);
        }

        return ServiceResponse.buildResponse(CD_SUCCESS,"Liste de retrait récupérée", listesRetraits);
    }

    public ServiceResponse<Retrait> getById(int id){
        Retrait retrait = daoRetrait.selectById(id);
        if (retrait == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Le lieu de retrait n'a pas été retrouvé", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS,"Lieu de retrait récupéré avec succes", retrait);
    }


    public ServiceResponse<Retrait> selectByArticleId(long idArticle) {
        Retrait retraitParArticle = daoRetrait.findByArticleId(idArticle);
        if (retraitParArticle == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Le lieu de retrait n'a pas pu être récupéré pour l'article", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, "Le lieu de retrait a bien été récupéré", retraitParArticle);
    }

    public ServiceResponse<List<Retrait>> selectByVille(String ville) {
        List<Retrait> retraitParVille = daoRetrait.findByVille(ville);
        if (retraitParVille.isEmpty()){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucun lieu de retrait récupéré pour cette ville", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS,"Liste de lieu de retrait par ville récupérée avec succes", retraitParVille);
    }

    public ServiceResponse<List<Retrait>> selectByCodePostal(String codePostal) {
        List<Retrait> retraitParCodePostal = daoRetrait.findByCodePostal(codePostal);
        if (retraitParCodePostal.isEmpty()){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucun lieu de retrait récupéré pour ce CP", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS,"Liste de lieu de retrait par CP  récupérée avec succes", retraitParCodePostal);
    }

    public ServiceResponse<Retrait> ajouterRetrait(Retrait retrait) {
        List<Retrait> ListesRetrait = daoRetrait.selectAll();

        for(Retrait r : ListesRetrait){
            if(r.getVille().equals(retrait.getVille())){
                if (r.getRue().equals(retrait.getRue())){
                    return ServiceResponse.buildResponse(CD_ERR_ALREADYHERE, "Il existe deja un retrait à cette rue dans cette ville", null);
                }
            }
        }
        daoRetrait.ajouterRetrait(retrait);
        return ServiceResponse.buildResponse(CD_SUCCESS, "Nouveau point de retrait créé avec succès", retrait);
    }

    public ServiceResponse<Retrait> supprimerRetrait(long idRetrait) {
        Retrait retraitASuprr = daoRetrait.selectById(idRetrait);

        if (retraitASuprr == null){
            return  ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La suppression de ce lieu de retrait n'a pas été réalisé", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS, "La suppression de ce lieu de retrait a été réalisé avec succes", retraitASuprr);
    }


}
