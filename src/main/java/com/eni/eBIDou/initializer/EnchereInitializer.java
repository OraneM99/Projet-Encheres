package com.eni.eBIDou.initializer;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleRepository;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereRepository;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
public class EnchereInitializer implements DataInitializer {

    private final EnchereRepository enchereRepository;
    private final ArticleRepository articleRepository;
    private final UtilisateurRepository utilisateurRepository;

    public EnchereInitializer(EnchereRepository enchereRepository,
                              ArticleRepository articleRepository,
                              UtilisateurRepository utilisateurRepository) {
        this.enchereRepository = enchereRepository;
        this.articleRepository = articleRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    @Transactional
    public void initialize() {
        if (isInitialized()) {
            System.out.println("Les enchères sont déjà initialisées");
            return;
        }

        if (articleRepository.count() == 0 || utilisateurRepository.count() == 0) {
            System.out.println("Impossible d'initialiser les enchères: les articles ou utilisateurs ne sont pas initialisés");
            return;
        }

        // Récupérer articles et utilisateurs
        List<Article> articles = articleRepository.findAll();
        List<UtilisateurBO> utilisateurs = utilisateurRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        // Créer des enchères pour les 5 premiers articles (s'ils existent)
        for (int i = 0; i < Math.min(5, articles.size()); i++) {
            Article article = articles.get(i);

            // Trouver un utilisateur qui n'est pas le vendeur pour faire une enchère
            UtilisateurBO encherisseur = utilisateurs.stream()
                    .filter(u -> !u.equals(article.getVendeur()))
                    .findFirst()
                    .orElse(null);

            if (encherisseur != null) {
                // Première enchère
                Enchere enchere1 = new Enchere();
                enchere1.setDateEnchere(now.minusDays(1).minusHours(i));
                enchere1.setMontant_enchere(article.getMiseAPrix() + 30);
                enchere1.setEncherisseur(encherisseur);
                enchere1.setArticleCible(article);
                enchereRepository.save(enchere1);

                // Trouver un autre utilisateur qui n'est ni le vendeur ni l'enchérisseur précédent
                UtilisateurBO encherisseur2 = utilisateurs.stream()
                        .filter(u -> !u.equals(article.getVendeur()) && !u.equals(encherisseur))
                        .findFirst()
                        .orElse(null);

                if (encherisseur2 != null) {
                    // Deuxième enchère (plus récente et plus élevée)
                    Enchere enchere2 = new Enchere();
                    enchere2.setDateEnchere(now.minusHours(i * 2 + 1));
                    enchere2.setMontant_enchere(enchere1.getMontant_enchere() + 50);
                    enchere2.setEncherisseur(encherisseur2);
                    enchere2.setArticleCible(article);
                    enchereRepository.save(enchere2);
                }
            }
        }

        System.out.println("Enchères initialisées avec succès");
    }

    @Override
    public boolean isInitialized() {
        return enchereRepository.count() > 0;
    }

    @Override
    public String getName() {
        return "EnchereInitializer";
    }
}