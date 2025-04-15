package com.eni.eBIDou.vues;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleDAOmock;
import com.eni.eBIDou.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class EncheresVueController {
    
    private ArticleDAOmock articleDAOmock;
    private final ArticleService articleService;
    
    public EncheresVueController(ArticleDAOmock articleDAOmock, ArticleService articleService) {
        this.articleDAOmock = articleDAOmock;
        this.articleService = articleService;
    }
    
    /**
     * Affichage de la liste des articles
     */
    @GetMapping({"/", "/accueil", "/encheres"})
    public String pageAccueil(Model model) {
        
        List<Article> articles = articleDAOmock.selectAll();
        model.addAttribute("articles", articles);
        return "page-accueil";
    }
}
