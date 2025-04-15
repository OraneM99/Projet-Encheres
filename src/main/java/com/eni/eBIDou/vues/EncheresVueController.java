package com.eni.eBIDou.vues;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleDAOmock;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieDAOmock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EncheresVueController {

    private final ArticleDAOmock articleDAOmock;
    private final CategorieDAOmock categorieDAOmock;

    @Autowired
    public EncheresVueController(ArticleDAOmock articleDAOmock, CategorieDAOmock categorieDAOmock) {
        this.articleDAOmock = articleDAOmock;
        this.categorieDAOmock = categorieDAOmock;
    }

    @GetMapping({"/", "/accueil", "/encheres"})
    public String pageAccueil(Model model,
                              @RequestParam(required = false) String nomArticle,
                              @RequestParam(required = false) Long categorieId) {

        // Récupération des articles
        List<Article> articles = articleDAOmock.selectAll();

        // Filtrage si nécessaire
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

        // Récupération des catégories
        List<Categorie> categories = categorieDAOmock.selectAll();

        // Ajout au modèle
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("nomArticle", nomArticle);
        model.addAttribute("categorieId", categorieId);

        return "page-accueil";
    }
}