package com.eni.eBIDou.categorie;

import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.*;




@Service
public class CategorieService {

    private CategorieIDAO daoCategorie;

    public CategorieService(CategorieIDAO daoCategorie) {
        this.daoCategorie = daoCategorie;
    }


    public ServiceResponse<List<Categorie>> selectAll() {
        List<Categorie> listeCategorie = daoCategorie.selectAll();

        // Erreur : 111 (Liste vide)
        if(listeCategorie.isEmpty()){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La liste des catégorie est vide", null);
        }
        // Succès 200
        return ServiceResponse.buildResponse(CD_SUCCESS,"La liste des catégories a été récupérée avec succès", listeCategorie);
    }
    
    public ServiceResponse<Categorie> selectById(long id) {
        Categorie categorie = daoCategorie.selectById(id);
        // Erreur 111
        if(categorie == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND,"cet id ne correspond à aucune categorie", null);
        }
        return ServiceResponse.buildResponse(CD_SUCCESS,"Une categorie a été recupéré par son id avec succes", categorie);
    }

    public ServiceResponse<Categorie> selectByName(String name) {
        Categorie categorie = daoCategorie.selectByName(name);
        if(categorie == null){
            return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "Aucune catégorie ne correspond à ces caractères", null);

        }
        return ServiceResponse.buildResponse(CD_SUCCESS, "Une catégorie a été recupérée grace à son nom", categorie);
    }


    public ServiceResponse<Categorie> addCategorie(Categorie categorie) {
        List<Categorie> listeCategorie = daoCategorie.selectAll();

        for(Categorie c : listeCategorie){
            if (categorie.getLibelle().equals(c.getLibelle())){
                return ServiceResponse.buildResponse(CD_ERR_ALREADYHERE, "Il existe deja une categorie avec ce libelle", null);

            }
        }
        daoCategorie.ajouterCategorie(categorie);
        return ServiceResponse.buildResponse(CD_SUCCESS,"Une categorie a été ajoutée avec succès", categorie);
    }


    public ServiceResponse<Categorie> updateCategorie(Categorie categorie) {
        List<Categorie> listeCategorie = daoCategorie.selectAll();

        for(Categorie c : listeCategorie){
            if(c.getNoCategorie() == categorie.getNoCategorie()){
                daoCategorie.ajouterCategorie(c);
                return ServiceResponse.buildResponse(CD_SUCCESS,"La categorie a été modifiée avec succès", categorie);
            }
        }
        return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND, "La modification a échoué, aucune catégorie associé n'a été trouvé", null);

    }


    public ServiceResponse<Categorie> deleteCategorie(long idCategorie) {
        List<Categorie> listeCategorie = daoCategorie.selectAll();

        for(Categorie c : listeCategorie){
            if(c.getNoCategorie() == idCategorie){
                daoCategorie.supprCategorie(idCategorie);
                return ServiceResponse.buildResponse(CD_SUCCESS,"Suppression de la catégorie avec succès", c);

            }
        }
        return ServiceResponse.buildResponse(CD_ERR_NOT_FOUND,"Aucune catégorie correspondant à cet id n'a pu être supprimée", null);
    }





}
