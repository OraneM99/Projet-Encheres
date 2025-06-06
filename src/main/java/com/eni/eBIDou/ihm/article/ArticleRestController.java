package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Profile("mock")
public class ArticleRestController {
    private final ArticleService articleService;

    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/getAllArticles")
    public ResponseEntity<ServiceResponse<List<Article>>> getAllArticles() {
        //Appeler le Métier
        ServiceResponse<List<Article>> serviceResponse = articleService.getAll();

        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getArticleByID")
    public ResponseEntity<ServiceResponse<Article>> getArticleById(long id) {
        ServiceResponse<Article> serviceResponse = articleService.getArticleById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getArticleByName")
    public ResponseEntity<ServiceResponse<List<Article>>> getArticlesByName(String name) {
        ServiceResponse<List<Article>> serviceResponse = articleService.getArticlesByName(name);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getArticleByCate")
    public ResponseEntity<ServiceResponse<List<Article>>> getArticlesByCategory(Categorie categorie) {
        ServiceResponse<List<Article>> serviceResponse = articleService.getArticlesByCategorie(categorie);
        return ResponseEntity.ok(serviceResponse);
    }



    @PostMapping("/article")
    public ResponseEntity<ServiceResponse<Article>> create(@RequestBody Article article) {
        ServiceResponse<Article> response = articleService.addArticle(article);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
