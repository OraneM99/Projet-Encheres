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
    public String detailVente(@PathVariable long id, Model model, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte) {

        //Récupérer l'article
        ServiceResponse<Article> article = articleService.getArticleById(id);
        Article articleAVendre = article.getData();

        //Récupérer le vendeur lié à l'article
        UtilisateurBO vendeur = articleAVendre.getVendeur();

        //Récupérer le retrait lié à l'article
        ServiceResponse<Retrait> retrait = retraitService.selectByArticleId(id);
        Retrait retraitData = retrait.getData();

        UtilisateurBO encherisseur = null;
        Enchere enchereData = null;

        if(articleAVendre.getEncheres() != null) {
            //Récupérer l'enchère l'enchère liée à l'article
            ServiceResponse<Enchere> enchere = enchereService.trouverMeilleureEnchere(articleAVendre.getNoArticle());
            enchereData = enchere.getData();
            if(enchereData != null) {
                encherisseur = enchereData.getEncherisseur();
            }


        }


        model.addAttribute("utilisateurConnecte", utilisateurConnecte);
        model.addAttribute("article", articleAVendre);
        model.addAttribute("retrait", retraitData);
        model.addAttribute("vendeur", vendeur);
        model.addAttribute("enchere", enchereData);
        model.addAttribute("encherisseur", encherisseur);

        return "detail-vente";
    }

    @PostMapping("/encherir/{id}")
    public String encherir(@PathVariable long id, @RequestParam int montant_enchere, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte, Model model) {

        //récupère l'utilisateur connecté qui veut enchérir
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);

        // récupérer l'id User
        long idEncherisseur = utilisateurConnecte.getNoUtilisateur();

        enchereService.placerEnchere(id, idEncherisseur, montant_enchere);

        return"redirect:/accueil";
    }


    @PostMapping("/supprimerArticle/{id}")
    public String annulerVente (@PathVariable long id, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte) {

        ServiceResponse<Article> article = articleService.getArticleById(id);

        if(article.getData().getVendeur().getNoUtilisateur() == utilisateurConnecte.getNoUtilisateur()) {
            articleService.deleteArticle(id);
        }

        return "redirect:/accueil";
    }


}
