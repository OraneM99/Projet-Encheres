package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.retrait.RetraitService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.CustomUserDetails;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class DetailVenteController {

    private final RetraitService retraitService;
    private final EnchereService enchereService;
    private final UtilisateurService utilisateurService;
    private ArticleService articleService;

    public DetailVenteController(ArticleService articleService, RetraitService retraitService, EnchereService enchereService, UtilisateurService utilisateurService) {
        this.articleService = articleService;
        this.retraitService = retraitService;
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/detail-vente/{id}")
    public String detailVente(@PathVariable long id, Model model) {

        //Récupérer l'article
        ServiceResponse<Article> article = articleService.getArticleById(id);
        Article articleAVendre = article.getData();

        //Récupérer le vendeur lié à l'article
        long idVendeur = articleAVendre.getVendeur().getNoUtilisateur();
        UtilisateurDTO vendeur = utilisateurService.findById(idVendeur);

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
        model.addAttribute("article", articleAVendre);
        model.addAttribute("retrait", retraitData);
        model.addAttribute("vendeur", vendeur);
        model.addAttribute("enchere", enchereData);
        model.addAttribute("encherisseur", encherisseur);

        return "detail-vente";
    }

    @PostMapping("/encherir/{id}")
    public String encherir(@PathVariable long id, @RequestParam int montant_enchere, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {



        // récupérer l'id User
        long idEncherisseur = customUserDetails.getUtilisateur().getNoUtilisateur();

        enchereService.placerEnchere(id, idEncherisseur, montant_enchere);

        return"redirect:/accueil";
    }


    @PostMapping("/supprimerArticle/{id}")
    public String annulerVente (@PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ServiceResponse<Article> article = articleService.getArticleById(id);

        if(article.getData().getVendeur().getNoUtilisateur() == customUserDetails.getUtilisateur().getNoUtilisateur()) {
            articleService.deleteArticle(id);
        }
        return "redirect:/accueil";
    }

    @GetMapping("/article/{id}/fin")
    public String afficherFinEnchere(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ServiceResponse<Article> articleData = articleService.getArticleById(id);
        Article article = articleData.getData();
        UtilisateurBO utilisateurConnecte = customUserDetails.getUtilisateur();

        ServiceResponse<Enchere> meilleureEnchereData = enchereService.trouverMeilleureEnchere(article.getNoArticle());
        Enchere meilleureEnchere = meilleureEnchereData.getData();
        UtilisateurBO vendeur = article.getVendeur();
        UtilisateurBO gagnant = meilleureEnchere != null ? meilleureEnchere.getEncherisseur() : null;

        model.addAttribute("article", article);
        model.addAttribute("vendeur", vendeur);
        model.addAttribute("encherisseur", gagnant);

        if(gagnant != null) {
            if (utilisateurConnecte.getNoUtilisateur().equals(gagnant.getNoUtilisateur())) {
                return "acquisition";
            }
        }

        if (utilisateurConnecte.getNoUtilisateur().equals(vendeur.getNoUtilisateur())) {
            return "fin-enchere";
        }
        return "redirect:/accueil";
    }

    @PostMapping("/articles/{id}/retrait-effectue")
    public String retraitEffectue(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Article article = articleService.getArticleById(id).getData();

        if (!article.getVendeur().getNoUtilisateur().equals(userDetails.getUtilisateur().getNoUtilisateur())) {
            return "redirect:/accueil";
        }

        articleService.marquerRetraitEffectue(id);

        // Redirection vers la même page, ou une autre vue de confirmation
        return "redirect:/article/" + id + "/fin";
    }

}
