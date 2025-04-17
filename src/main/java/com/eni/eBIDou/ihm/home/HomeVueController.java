package com.eni.eBIDou.ihm.home;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.data.EtatVente;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HomeVueController {

    private final ArticleService articleService;
    private final CategorieService categorieService;

    public HomeVueController(ArticleService articleService, CategorieService categorieService) {
        this.articleService = articleService;
        this.categorieService = categorieService;
    }

    @GetMapping({"/", "/accueil"})
    public String pageAccueil(Model model,
                              @RequestParam(required = false) String nomArticle,
                              @RequestParam(required = false) Long categorieId) {

        List<Article> articles = articleService.getAll().getData();

        // Récupérer le nom de la catégorie sélectionnée si applicable
        String categorieNom = null;
        if (categorieId != null && categorieId > 0) {
            // Trouver la catégorie par son ID
            Optional<Categorie> optionalCategorie = categorieService.selectAll().getData()
                    .stream()
                    .filter(c -> c.getNoCategorie() == categorieId)
                    .findFirst();

            if (optionalCategorie.isPresent()) {
                categorieNom = optionalCategorie.get().getLibelle();
            }
        }

        // Filtrer les articles selon les critères de recherche
        if (nomArticle != null && !nomArticle.isEmpty()) {
            articles = articles.stream()
                    .filter(a -> a.getNomArticle().toLowerCase().contains(nomArticle.toLowerCase()))
                    .collect(Collectors.toList());
        } else if (categorieId != null && categorieId > 0) {
            articles = articles.stream()
                    .filter(a -> a.getCategorieArticle() != null &&
                            a.getCategorieArticle().getNoCategorie() == categorieId)
                    .collect(Collectors.toList());
        }

        // Récupérer les articles en vedette (enchères en cours)
        List<Article> articlesEnVedette = articleService.getAll().getData().stream()
                .filter(a -> a.getEtatVente() == EtatVente.EN_COURS)
                .filter(a -> a.getDateFinEncheres().isAfter(LocalDateTime.now()))
                .limit(3) // Limiter à 3 articles pour la page d'accueil
                .collect(Collectors.toList());

        // Récupérer toutes les catégories
        List<Categorie> categories = categorieService.selectAll().getData();

        // Ajouter les attributs au modèle
        model.addAttribute("articles", articles);
        model.addAttribute("articlesEnVedette", articlesEnVedette);
        model.addAttribute("categories", categories);
        model.addAttribute("nomArticle", nomArticle);
        model.addAttribute("categorieId", categorieId);
        model.addAttribute("categorieNom", categorieNom);

        return "accueil";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        model.addAttribute("message", "Vous êtes déconnecté");
        return "redirect:/accueil";
    }
}