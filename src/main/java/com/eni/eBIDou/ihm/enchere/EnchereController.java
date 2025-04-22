package com.eni.eBIDou.ihm.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurNotFoundException;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@Profile("jpa")
public class EnchereController {

    private final EnchereService enchereService;
    private final UtilisateurService utilisateurService;
    private final CategorieService categorieService;
    private final ArticleService articleService;

    public EnchereController(EnchereService enchereService, 
                             UtilisateurService utilisateurService,
                             CategorieService categorieService,
                             ArticleService articleService) {
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
        this.categorieService = categorieService;
        this.articleService = articleService;
    }

    @GetMapping("/encheres")
    public String getAll(Model model,
                         @RequestParam(required = false) String nomArticle,
                         @RequestParam(required = false) Long id,
                         @RequestParam(required = false) String typeRecherche) {
        try {
            
            Categorie categorie = null;
            String categorieNom = null;
            if (id != null && id > 0) {
                categorie = categorieService.selectById(id).getData();
                categorieNom = categorie != null ? categorie.getLibelle() : null;
            }
            
            ServiceResponse<List<Article>> serviceResponse = articleService.rechercherArticles(nomArticle, categorie);

            // Récupérer les catégories
            List<Categorie> categories = categorieService.selectAll().getData();

            // Ajouter les attributs au modèle
            model.addAttribute("articles", serviceResponse.getData() != null ? serviceResponse.getData() : new ArrayList<>());
            model.addAttribute("categories", categories);
            model.addAttribute("nomArticle", nomArticle);
            model.addAttribute("categorieId", id);
            model.addAttribute("categorieNom", categorieNom);
            model.addAttribute("message", serviceResponse.getMessage());

            // Ajouter l'utilisateur connecté
            addConnectedUserToModel(model);

            return "encheres";
        } catch (Exception e) {
            model.addAttribute("message", "Erreur lors du chargement des enchères: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/encheres/{id}")
    public ResponseEntity<ServiceResponse<Enchere>> getEnchereById(@PathVariable long id) {
        ServiceResponse<Enchere> serviceResponse = enchereService.getById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/encheres/article/{articleId}")
    public ResponseEntity<ServiceResponse<Enchere>> getEnchereByArticle(@PathVariable Long articleId) {
        Article article = new Article();
        article.setNoArticle(articleId);
        ServiceResponse<Enchere> serviceResponse = enchereService.getByArticleCible(article);
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/encheres")
    public ResponseEntity<ServiceResponse<Enchere>> placerEnchere(
            @RequestParam Long idArticle,
            @RequestParam Long idUtilisateur,
            @RequestParam int montant) {
        ServiceResponse<Enchere> serviceResponse = enchereService.placerEnchere(idArticle, idUtilisateur, montant);
        return ResponseEntity.ok(serviceResponse);
    }

    @DeleteMapping("/encheres/{idEnchere}")
    public ResponseEntity<ServiceResponse<Enchere>> supprimerEnchere(@PathVariable Long idEnchere) {
        ServiceResponse<Enchere> serviceResponse = enchereService.supprimerEnchere(idEnchere);
        return ResponseEntity.ok(serviceResponse);
    }
    
    @ExceptionHandler(UtilisateurNotFoundException.class)
    public String handleUtilisateurNotFoundException(UtilisateurNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    // Méthode utilitaire pour ajouter l'utilisateur connecté au modèle
    private void addConnectedUserToModel(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getName())) {
                return;
            }

            // Récupérer le pseudo de l'utilisateur connecté (nom d'utilisateur)
            String pseudo = authentication.getName();

            // Ajouter seulement le pseudo au modèle
            model.addAttribute("utilisateurConnecte", pseudo);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du pseudo de l'utilisateur au modèle: " + e.getMessage());
        }
    }
}