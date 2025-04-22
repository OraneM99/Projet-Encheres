package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.retrait.RetraitService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DetailVenteController {

    private final RetraitService retraitService;
    private final EnchereService enchereService;
    private ArticleService articleService;

    public DetailVenteController(ArticleService articleService, RetraitService retraitService, EnchereService enchereService) {
        this.articleService = articleService;
        this.retraitService = retraitService;
        this.enchereService = enchereService;
    }

    @GetMapping("/detail-vente/{id}")
    public String detailVente(@PathVariable long id, Model model) {

        //Récupérer l'article
        ServiceResponse<Article> article = articleService.getArticleById(id);
        Article articleAVendre = article.getData();

        //Récupérer le vendeur lié à l'article
        UtilisateurBO vendeur = articleAVendre.getVendeur();

        //Récupérer le retrait lié à l'article
        ServiceResponse<Retrait> retrait = retraitService.selectByArticleId(id);
        Retrait retraitData = retrait.getData();

        //Récupérer l'enchère l'enchère liée à l'article
        ServiceResponse<Enchere> enchere = enchereService.trouverMeilleureEnchere(articleAVendre.getNoArticle());
        Enchere enchereData = enchere.getData();

        UtilisateurBO encherisseur = null;
        if(enchereData != null ) {
            encherisseur = enchereData.getEncherisseur();
        }




        model.addAttribute("article", articleAVendre);
        model.addAttribute("retrait", retraitData);
        model.addAttribute("vendeur", vendeur);
        model.addAttribute("enchere", enchereData);
        model.addAttribute("encherisseur", encherisseur);



        return "detail-vente";
    }

    @PostMapping("/encherir/{id}")
    public String encherir(@PathVariable long id, @RequestParam int montant_enchere, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte, Model model) {




        //nécessité de récupérer l'utilisateur connecte qui veut enchérir
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);

        // Récuperer l'article visité
        ServiceResponse<Article> article = articleService.getArticleById(id);

        System.out.println("UtilisateurConnecte: "+ utilisateurConnecte);
        System.out.println("Article: "+ article.getData());
        System.out.println("Montant de l'enchere" + montant_enchere);


        // Vérifier que le montant de l'enchere est plus haut que le montant de départ de l'article
        if(montant_enchere > article.getData().getMiseAPrix()){

            // vérifier que l'utilisateur a bien un crédit suffisant pour couvrir le montant
            if(utilisateurConnecte.getCredit() >= montant_enchere) {
                // récupérer l'id de l'article
                long idArticle = article.getData().getNoArticle();
                // récupérer l'id User
                long idEncherisseur = utilisateurConnecte.getNoUtilisateur();

                ServiceResponse<Enchere> newEnchere = enchereService.placerEnchere(idArticle, idEncherisseur, montant_enchere);

            }

        }


        return"redirect:/accueil/";
    }


}
