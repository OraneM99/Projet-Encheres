package com.eni.eBIDou.vues;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.eni.eBIDou.service.ServiceConstant.CD_SUCCESS;

@Controller
public class EncheresVueController {
    
//    private CategorieService categorieService;
    private final ArticleService articleService;
    
    public EncheresVueController(CategorieService categorieService, ArticleService articleService) {
        this.categorieService = categorieService;
        this.articleService = articleService;
    }
    
    /**
     * Affichage de la liste des articles
     */
    @GetMapping({"/", "/accueil", "/encheres"})
    public String pageAccueil(Model model,
                              @RequestParam(required = false) String nomArticle,
                              @RequestParam(required = false) Long categorieId) {

        ServiceResponse<List<Article>> articlesResponse;

        if (nomArticle != null && !nomArticle.trim().isEmpty()) {
            articlesResponse = articleService.getArticlesByName(nomArticle);
        } else if (categorieId != null && categorieId > 0) {
            ServiceResponse<Categorie> categorieResponse = categorieService.getCategorieById(categorieId);

            if (CD_SUCCESS.equals(categorieResponse.code)) {
                articlesResponse = articleService.getArticlesByCategorie(categorieResponse.getData());
            } else {
                articlesResponse = articleService.getAll();
            }
        } else {
            articlesResponse = articleService.getAll();
        }

        // Ajouter la réponse complète au modèle pour l'affichage des messages
        model.addAttribute("articlesResponse", articlesResponse);

        // Ajouter les articles au modèle
        model.addAttribute("articles", articlesResponse.getData());
        model.addAttribute("nomArticle", nomArticle);
        model.addAttribute("categorieId", categorieId);
        
        ServiceResponse<List<Categorie>> categoriesResponse = categorieService.getAllCategories();

        // Ajouter la réponse complète des catégories au modèle pour l'affichage des messages d'erreurs
        model.addAttribute("categoriesResponse", categoriesResponse);

        // Ajouter les catégories au modèle
        model.addAttribute("categories", categoriesResponse.getData());

        return "page-accueil";
    }
}
