package com.eni.eBIDou.ihm.home;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.service.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eni.eBIDou.service.ServiceConstant.CD_SUCCESS;

@Controller
public class HomeVueController {

    private final ArticleService articleService;
    private final CategorieService categorieService;
    private final EnchereService enchereService;

    public HomeVueController(ArticleService articleService, CategorieService categorieService, EnchereService enchereService) {
        this.articleService = articleService;
        this.categorieService = categorieService;
        this.enchereService = enchereService;
    }

    @GetMapping({"/", "/accueil"})
    public String pageAccueil(Model model,
                              @RequestParam(required = false) String nomArticle,
                              @RequestParam(required = false) Long categorieId) {

        // Récupérer tous les articles dont l'etat est En_Cours
        List<Article> articles = articleService.getAll().getData().stream()
                .filter(article -> article.getEtatVente() == EtatVente.EN_COURS)
                .collect(Collectors.toList());

        // Récupérer toutes les enchères
        ServiceResponse<List<Enchere>> enchereResponse = enchereService.getAll();
        List<Enchere> encheres = CD_SUCCESS.equals(enchereResponse.code)
                ? enchereResponse.data
                : List.of();

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

            // Filtrer également les enchères correspondantes si nécessaire
            encheres = encheres.stream()
                    .filter(e -> e.getArticleCible().getNomArticle().toLowerCase()
                            .contains(nomArticle.toLowerCase()))
                    .collect(Collectors.toList());
        } else if (categorieId != null && categorieId > 0) {
            articles = articles.stream()
                    .filter(a -> a.getCategorieArticle() != null &&
                            a.getCategorieArticle().getNoCategorie() == categorieId)
                    .collect(Collectors.toList());

            // Filtrer également les enchères par catégorie si nécessaire
            encheres = encheres.stream()
                    .filter(e -> e.getArticleCible().getCategorieArticle() != null &&
                            e.getArticleCible().getCategorieArticle().getNoCategorie() == categorieId)
                    .collect(Collectors.toList());
        }

        // Récupérer les articles en vedette (enchères en cours)
        List<Article> articlesEnVedette = articles.stream()
                .limit(3) // Limiter à 3 articles pour la page d'accueil
                .collect(Collectors.toList());

        // Récupérer toutes les catégories
        List<Categorie> categories = categorieService.selectAll().getData();

        // Ajouter les attributs au modèle
        model.addAttribute("articles", articles);
        model.addAttribute("encheres", encheres);
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