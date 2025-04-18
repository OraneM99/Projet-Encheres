package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.retrait.RetraitService;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VenteController {
    
    private final CategorieService categorieService;
    private final ArticleService articleService;
    private final UtilisateurMapper utilisateurMapper;
    private final RetraitService retraitService;

    public VenteController(CategorieService categorieService,
                           ArticleService articleService,
                           UtilisateurMapper utilisateurMapper,
                           RetraitService retraitService) {
        this.categorieService = categorieService;
        this.articleService = articleService;
        this.utilisateurMapper = utilisateurMapper;
        this.retraitService = retraitService;
    }

    @GetMapping("/nouvelle-vente")
    public String afficherNewVentes(Model model, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte) {

        //recuperer l'utilisateur Connecte
        model.addAttribute("utilisateur", utilisateurMapper.toDto(utilisateurConnecte));

        model.addAttribute("article", new Article());

        //recuperer la liste des categories
        List<Categorie> categories = categorieService.selectAll().getData();
        model.addAttribute("categories", categories);

        //recupérer la liste des points de retrait
        List<Retrait> retraitList = retraitService.getAll().getData();
        model.addAttribute("retraits", retraitList);

        return "nouvelle-vente";
    }
    
    @PostMapping("/creer")
    public String creerVente(@ModelAttribute Article article, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte) {

        //lié l'article à l'utilisateur qui créé la vente
        article.setVendeur(utilisateurConnecte);

        //sauvegardé l'article dans la base de donnée
        articleService.addArticle(article);

        return "redirect:/accueil";
    }
}

