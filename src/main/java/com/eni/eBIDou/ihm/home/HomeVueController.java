package com.eni.eBIDou.ihm.home;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
        
        List<Categorie> categories = categorieService.selectAll().getData();
        
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("nomArticle", nomArticle);
        model.addAttribute("categorieId", categorieId);

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