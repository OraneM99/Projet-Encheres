package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieIDAO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleDAOmock implements ArticleIDAO {

    private CategorieIDAO daoCategorie;

    public List<Article> articlesLists = new ArrayList<>();


    public ArticleDAOmock(CategorieIDAO daoCategorie) {
        this.daoCategorie = daoCategorie;

        List<Categorie> listCategorieMock = daoCategorie.selectAll();

        Article article1 = new Article(1L, "Trombone", "Ca permet de pas regrouper des feuilles", LocalDateTime.of(2025,04,01,10,05) , 5);
        article1.setCategorieArticle(listCategorieMock.get(2));

        Article article2 = new Article(2L, "bout de scotch", "Peut encore servir", LocalDateTime.of(2025,04,01,10,05 ),1);
        article2.setCategorieArticle(listCategorieMock.get(2));

        Article article3 = new Article(3L, "Cuillère cassée", "Faudra la laver", LocalDateTime.of(2025,04,01,10,05), 1);
        article3.setCategorieArticle(listCategorieMock.get(0));

        Article article4 = new Article(4L, "éponge salle", "On ne sait jamais", LocalDateTime.of(2025,04,01,10,05) , 1);
        article4.setCategorieArticle(listCategorieMock.get(0));

        articlesLists.add(article1);
        articlesLists.add(article2);
        articlesLists.add(article3);
        articlesLists.add(article4);



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
    public List<Article> selectByName(String name) {
        List<Article> articlesList = new ArrayList<>();
        for (Article article : articlesLists) {
            if (article.getNomArticle().contains(name)) {
                articlesLists.add(article);
            }
        }
        return articlesList;
    }

    @Override
    public List<Article> selectByCategorie(Categorie categorie) {
        List<Article> articleByCate = new ArrayList<>();

        for (Article article : articlesLists) {
            if (article.getCategorieArticle().equals(categorie)) {
                articleByCate.add(article);
            }
        }
        return articleByCate;
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
