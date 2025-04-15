package com.eni.eBIDou.article;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleDAOmock implements ArticleIDAO {

    public List<Article> articlesLists = new ArrayList<>();

    public ArticleDAOmock() {

        articlesLists.add(new Article(1L, "Trombone", "Ca permet de pas regrouper des feuilles", LocalDateTime.of(2025,04,01,10,05) , 5));
        articlesLists.add(new Article(2L, "bout de scotch", "Peut encore servir", LocalDateTime.of(2025,04,01,10,05), 2));
        articlesLists.add(new Article(3L, "Cuillère cassée", "Faudra la laver", LocalDateTime.of(2025,04,01,10,05), 1));
        articlesLists.add(new Article(4L, "éponge salle", "On ne sait jamais", LocalDateTime.of(2025,04,01,10,05) , 1));

    }

    @Override
    public List<Article> selectAll() {
        return articlesLists;
    }

    @Override
    public Article selectById(long id) {
        for (Article article : articlesLists) {
            if (article.getNoArticle() == id) {
                return article;
            }
        }
        return null;
    }

    @Override
    public Article selectByName(String name) {
        for (Article article : articlesLists) {
            if (article.getNomArticle().equals(name)) {
                return article;
            }
        }
        return null;
    }

    @Override
    public void ajouterArticle(Article article) {
        articlesLists.add(article);

    }

    @Override
    public void updateArticle(Article article) {
        for(Article oldArticle : articlesLists) {
            if (oldArticle.getNoArticle() == article.getNoArticle()) {
                articlesLists.remove(oldArticle);
                articlesLists.add(article);
            }
        }
    }

    @Override
    public void deleteArticle(long idArticle) {
        for(Article oldArticle : articlesLists) {
            if (oldArticle.getNoArticle() == idArticle) {
                articlesLists.remove(oldArticle);
            }
        }
    }

}
