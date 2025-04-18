package com.eni.eBIDou.initializer;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleRepository;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieRepository;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleInitializer implements DataInitializer {

    private final ArticleRepository articleRepository;
    private final CategorieRepository categorieRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ArticleInitializer(ArticleRepository articleRepository,
                              CategorieRepository categorieRepository,
                              UtilisateurRepository utilisateurRepository) {
        this.articleRepository = articleRepository;
        this.categorieRepository = categorieRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    @Transactional
    public void initialize() {
        if (isInitialized()) {
            System.out.println("Les articles sont déjà initialisés");
            return;
        }

        if (categorieRepository.count() == 0 || utilisateurRepository.count() == 0) {
            System.out.println("Impossible d'initialiser les articles: les catégories ou utilisateurs ne sont pas initialisés");
            return;
        }

        // Récupérer les catégories et utilisateurs
        List<Categorie> categories = categorieRepository.findAll();
        List<UtilisateurBO> utilisateurs = utilisateurRepository.findAll();

        if (categories.isEmpty() || utilisateurs.isEmpty()) {
            System.out.println("Pas de catégories ou d'utilisateurs disponibles");
            return;
        }

        // Initialisation des articles
        LocalDateTime now = LocalDateTime.now();

        // Articles Informatique
        Article article1 = new Article();
        article1.setNomArticle("MacBook Pro 2023");
        article1.setDescription("Ordinateur portable Apple, 16 pouces, 32Go RAM, 1To SSD");
        article1.setDateDebutEncheres(now.minusDays(1));
        article1.setDateFinEncheres(now.plusDays(6));
        article1.setMiseAPrix(1500);
        article1.setPrixVente(0);
        article1.setEtatVente(EtatVente.EN_COURS);
        article1.setVendeur(utilisateurs.get(1)); // user1
        article1.setCategorieArticle(getCategorieByLibelle(categories, "Informatique"));
        article1.setEncheres(new ArrayList<>());
        articleRepository.save(article1);

        // Autres articles...

        System.out.println("Articles initialisés avec succès");
    }

    private Categorie getCategorieByLibelle(List<Categorie> categories, String libelle) {
        return categories.stream()
                .filter(c -> c.getLibelle().equals(libelle))
                .findFirst()
                .orElse(categories.get(0));
    }

    @Override
    public boolean isInitialized() {
        return articleRepository.count() > 0;
    }

    @Override
    public String getName() {
        return "ArticleInitializer";
    }
}