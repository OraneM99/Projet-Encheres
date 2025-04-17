package com.eni.eBIDou.config;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleRepository;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieRepository;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CategorieRepository categorieRepository,
                                   ArticleRepository articleRepository,
                                   UtilisateurRepository utilisateurRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Vérifier si la base de données est déjà initialisée
            if (categorieRepository.count() > 0) {
                System.out.println("La base de données contient déjà des données, initialisation ignorée.");
                return;
            }

            System.out.println("Initialisation de la base de données...");
            initData(categorieRepository, articleRepository, utilisateurRepository, passwordEncoder);
            System.out.println("Initialisation de la base de données terminée avec succès!");
        };
    }

    @Transactional
    public void initData(CategorieRepository categorieRepository,
                         ArticleRepository articleRepository,
                         UtilisateurRepository utilisateurRepository,
                         PasswordEncoder passwordEncoder) {

        // Création des catégories
        Categorie catInfo = new Categorie();
        catInfo.setLibelle("Informatique");
        catInfo.setArticles(new ArrayList<>());
        categorieRepository.save(catInfo);

        Categorie catAmeub = new Categorie();
        catAmeub.setLibelle("Ameublement");
        catAmeub.setArticles(new ArrayList<>());
        categorieRepository.save(catAmeub);

        Categorie catVet = new Categorie();
        catVet.setLibelle("Vêtement");
        catVet.setArticles(new ArrayList<>());
        categorieRepository.save(catVet);

        Categorie catSport = new Categorie();
        catSport.setLibelle("Sport & Loisirs");
        catSport.setArticles(new ArrayList<>());
        categorieRepository.save(catSport);

        System.out.println("Catégories initialisées");

        // Création des utilisateurs
        UtilisateurBO admin = new UtilisateurBO();
        admin.setPseudo("admin");
        admin.setNom("Admin");
        admin.setPrenom("Admin");
        admin.setEmail("admin@ebidou.fr");
        admin.setTelephone("0123456789");
        admin.setRue("1 Rue de l'Admin");
        admin.setCodePostal("75000");
        admin.setVille("Paris");
        admin.setCredit(500);
        admin.setAdministrateur(true);
        admin.setMotDePasse(passwordEncoder.encode("admin123"));
        utilisateurRepository.save(admin);

        UtilisateurBO user1 = new UtilisateurBO();
        user1.setPseudo("user1");
        user1.setNom("Dupont");
        user1.setPrenom("Jean");
        user1.setEmail("jean.dupont@ebidou.fr");
        user1.setTelephone("0612345678");
        user1.setRue("15 Rue des Lilas");
        user1.setCodePostal("44000");
        user1.setVille("Nantes");
        user1.setCredit(200);
        user1.setAdministrateur(false);
        user1.setMotDePasse(passwordEncoder.encode("user123"));
        utilisateurRepository.save(user1);

        UtilisateurBO user2 = new UtilisateurBO();
        user2.setPseudo("user2");
        user2.setNom("Martin");
        user2.setPrenom("Sophie");
        user2.setEmail("sophie.martin@ebidou.fr");
        user2.setTelephone("0698765432");
        user2.setRue("8 Avenue des Roses");
        user2.setCodePostal("69000");
        user2.setVille("Lyon");
        user2.setCredit(350);
        user2.setAdministrateur(false);
        user2.setMotDePasse(passwordEncoder.encode("user123"));
        utilisateurRepository.save(user2);

        UtilisateurBO user3 = new UtilisateurBO();
        user3.setPseudo("user3");
        user3.setNom("Lefebvre");
        user3.setPrenom("Pierre");
        user3.setEmail("pierre.lefebvre@ebidou.fr");
        user3.setTelephone("0678901234");
        user3.setRue("23 Boulevard du Commerce");
        user3.setCodePostal("33000");
        user3.setVille("Bordeaux");
        user3.setCredit(150);
        user3.setAdministrateur(false);
        user3.setMotDePasse(passwordEncoder.encode("user123"));
        utilisateurRepository.save(user3);

        UtilisateurBO user4 = new UtilisateurBO();
        user4.setPseudo("user4");
        user4.setNom("Dubois");
        user4.setPrenom("Marie");
        user4.setEmail("marie.dubois@ebidou.fr");
        user4.setTelephone("0645678901");
        user4.setRue("5 Place du Marché");
        user4.setCodePostal("13000");
        user4.setVille("Marseille");
        user4.setCredit(250);
        user4.setAdministrateur(false);
        user4.setMotDePasse(passwordEncoder.encode("user123"));
        utilisateurRepository.save(user4);

        System.out.println("Utilisateurs initialisés");

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
        article1.setVendeur(user1);
        article1.setCategorieArticle(catInfo);
        article1.setEncheres(new ArrayList<>());
        articleRepository.save(article1);

        Article article2 = new Article();
        article2.setNomArticle("iPad Air 2022");
        article2.setDescription("Tablette Apple, 64Go, Wifi + Cellular");
        article2.setDateDebutEncheres(now.minusDays(2));
        article2.setDateFinEncheres(now.plusDays(4));
        article2.setMiseAPrix(500);
        article2.setPrixVente(0);
        article2.setEtatVente(EtatVente.EN_COURS);
        article2.setVendeur(user2);
        article2.setCategorieArticle(catInfo);
        article2.setEncheres(new ArrayList<>());
        articleRepository.save(article2);

        Article article3 = new Article();
        article3.setNomArticle("Canapé d'angle");
        article3.setDescription("Canapé 5 places en tissu gris, convertible");
        article3.setDateDebutEncheres(now.minusDays(5));
        article3.setDateFinEncheres(now.plusDays(2));
        article3.setMiseAPrix(600);
        article3.setPrixVente(750);
        article3.setEtatVente(EtatVente.EN_COURS);
        article3.setVendeur(user4);
        article3.setCategorieArticle(catAmeub);
        article3.setEncheres(new ArrayList<>());
        articleRepository.save(article3);

        Article article4 = new Article();
        article4.setNomArticle("Veste en cuir");
        article4.setDescription("Veste en cuir noir, taille L, marque Zara");
        article4.setDateDebutEncheres(now.minusDays(4));
        article4.setDateFinEncheres(now.plusDays(3));
        article4.setMiseAPrix(80);
        article4.setPrixVente(120);
        article4.setEtatVente(EtatVente.EN_COURS);
        article4.setVendeur(user3);
        article4.setCategorieArticle(catVet);
        article4.setEncheres(new ArrayList<>());
        articleRepository.save(article4);

        Article article5 = new Article();
        article5.setNomArticle("Vélo électrique");
        article5.setDescription("Vélo électrique urbain, autonomie 80km");
        article5.setDateDebutEncheres(now.minusDays(6));
        article5.setDateFinEncheres(now.plusDays(1));
        article5.setMiseAPrix(900);
        article5.setPrixVente(1100);
        article5.setEtatVente(EtatVente.EN_COURS);
        article5.setVendeur(user1);
        article5.setCategorieArticle(catSport);
        article5.setEncheres(new ArrayList<>());
        articleRepository.save(article5);

        System.out.println("Articles initialisés");
    }
}