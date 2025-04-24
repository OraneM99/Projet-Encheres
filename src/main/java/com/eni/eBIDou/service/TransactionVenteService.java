package com.eni.eBIDou.service;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleRepository;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static com.eni.eBIDou.service.ServiceConstant.CD_ERR_TCH;
import static com.eni.eBIDou.service.ServiceConstant.CD_SUCCESS;

@Service
public class TransactionVenteService {

    private final EnchereService enchereService;
    private final ArticleRepository articleRepository;
    private final UtilisateurService utilisateurService;

    public TransactionVenteService(EnchereService enchereService, ArticleRepository articleRepository, UtilisateurService utilisateurService) {
        this.enchereService = enchereService;
        this.articleRepository = articleRepository;
        this.utilisateurService = utilisateurService;

    }

    @Transactional
    public ServiceResponse<Article> traiterRetrait(long articleId) {
        Article article = articleRepository.getReferenceById(articleId);
        if(article.getEtatVente() != EtatVente.TERMINEE){
            return ServiceResponse.buildResponse(CD_ERR_TCH, "Le retrait ne peut être confirmé que pour les articles dont la vente est terminée.", null);
        }

        // 1. Trouver la meilleure enchère
        ServiceResponse<Enchere> response = enchereService.trouverMeilleureEnchere(articleId);
        if (!CD_SUCCESS.equals(response.getCode()) || response.getData() == null) {
            return ServiceResponse.buildResponse(CD_ERR_TCH, "Aucune enchère gagnante trouvée.", null );
        }

        Enchere gagnante = response.getData();
        int montant = gagnante.getMontant_enchere();


        //2. Créditer le vendeur
        UtilisateurBO vendeur = article.getVendeur();
        int nouveauCredit = vendeur.getCredit() + montant;
        utilisateurService.updateCredit(vendeur.getNoUtilisateur(),  nouveauCredit);

        //3. Mettre a jour l'etat de l'article
        article.setEtatVente(EtatVente.RETRAIT_EFFECTUE );
        articleRepository.save(article);

        return ServiceResponse.buildResponse(CD_SUCCESS, "Transaction de fin de vente réalisée avec succès.", article);

    }
}
