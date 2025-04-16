package com.eni.eBIDou.vues;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleDAOmock;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieDAOmock;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Profile("mock")
public class HomeVueController {

    private final ArticleDAOmock articleDAOmock;
    private final CategorieDAOmock categorieDAOmock;

    @Autowired
    public HomeVueController(ArticleDAOmock articleDAOmock, CategorieDAOmock categorieDAOmock) {
        this.articleDAOmock = articleDAOmock;
        this.categorieDAOmock = categorieDAOmock;
    }

    @GetMapping({"/", "/accueil"})
    public String pageAccueil(Model model,
                              @RequestParam(required = false) String nomArticle,
                              @RequestParam(required = false) Long categorieId) {

        List<Article> articles = articleDAOmock.selectAll();
        
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
        
        List<Categorie> categories = categorieDAOmock.selectAll();
        
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("nomArticle", nomArticle);
        model.addAttribute("categorieId", categorieId);

        return "page-accueil";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        model.addAttribute("message", "Vous êtes déconnecté");
        return "redirect:/accueil";
    }
}