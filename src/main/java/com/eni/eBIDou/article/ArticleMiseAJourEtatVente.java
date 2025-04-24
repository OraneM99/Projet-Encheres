package com.eni.eBIDou.article;

import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleMiseAJourEtatVente {

    private final ArticleService articleService;

    public ArticleMiseAJourEtatVente(ArticleService articleService) {
        this.articleService = articleService;
    }

    //@Scheduled(cron = "0 0 0 * * *") // Tous les jours à minuit
    @Scheduled(fixedRate = 10000) // toutes les 10 secondes
    public void mettreAJourEtatsArticles() {
        System.out.println("Mise à jour des articles en cours...");
        ServiceResponse<List<Article>> articles = articleService.verifierEtMettreAjourEtatVente();
        System.out.println(articles.code);
    }
}
