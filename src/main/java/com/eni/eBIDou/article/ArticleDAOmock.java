package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieIDAO;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.pagination.PaginationUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Profile("dev")
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
                articlesList.add(article);
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
        articlesLists.removeIf(oldArticle -> oldArticle.getNoArticle() == idArticle);
    }

    @Override
    public List<Article> selectEncheresEnCours() {
        return List.of();
    }

    @Override
    public List<Article> selectByNameAndCategorie(String nom, Categorie categorie) {
        List<Article> resultat = new ArrayList<>();
        for (Article article : articlesLists) {
            if (article.getNomArticle().toLowerCase().contains(nom.toLowerCase())
                    && article.getCategorieArticle().equals(categorie)) {
                resultat.add(article);
            }
        }
        return resultat;
    }

    @Override
    public Optional<Article> findByEncherisseurId(Long noUtilisateur) {
        return Optional.empty();
    }

    // Nouvelles méthodes avec pagination
    @Override
    public Page<Article> selectAllPaginated(Pageable pageable) {
        return PaginationUtils.getPageFromList(articlesLists, pageable);
    }

    @Override
    public Page<Article> selectByNamePaginated(String name, Pageable pageable) {
        List<Article> filtered = articlesLists.stream()
                .filter(article -> article.getNomArticle().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return PaginationUtils.getPageFromList(filtered, pageable);
    }

    @Override
    public Page<Article> selectByCategoriePaginated(Categorie categorie, Pageable pageable) {
        List<Article> filtered = articlesLists.stream()
                .filter(article -> article.getCategorieArticle().equals(categorie))
                .collect(Collectors.toList());
        return PaginationUtils.getPageFromList(filtered, pageable);
    }

    @Override
    public Page<Article> selectByNameAndCategoriePaginated(String nom, Categorie categorie, Pageable pageable) {
        List<Article> filtered = articlesLists.stream()
                .filter(article -> article.getNomArticle().toLowerCase().contains(nom.toLowerCase())
                        && article.getCategorieArticle().equals(categorie))
                .collect(Collectors.toList());
        return PaginationUtils.getPageFromList(filtered, pageable);
    }

    @Override
    public Page<Article> selectEncheresEnCoursPaginated(EtatVente etatVente, Pageable pageable) {
        List<Article> filtered = articlesLists.stream()
                .filter(article -> article.getEtatVente() == etatVente)
                .collect(Collectors.toList());
        return PaginationUtils.getPageFromList(filtered, pageable);
    }
}
