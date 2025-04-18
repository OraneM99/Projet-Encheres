package com.eni.eBIDou.ihm.enchere;

import com.eni.eBIDou.article.Article;
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

import java.util.List;

@Controller
@Profile("jpa")
public class EnchereController {

    private final EnchereService enchereService;
    private final UtilisateurService utilisateurService;

    public EnchereController(EnchereService enchereService, UtilisateurService utilisateurService) {
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/encheres")
    public String getAll(Model model, RedirectAttributes redirectAttributes) {
        try {
            ServiceResponse<List<Enchere>> serviceResponse = enchereService.getAll();
            
            System.out.println("Code de réponse du service: " + serviceResponse.code);
            System.out.println("Message du service: " + serviceResponse.message);
            System.out.println("Nombre d'enchères reçues: " +
                    (serviceResponse.data != null ? serviceResponse.data.size() : "null"));

            model.addAttribute("encheres", serviceResponse.data != null ? serviceResponse.data : List.of());
            model.addAttribute("message", serviceResponse.message);
            addConnectedUserToModel(model);

            return "encheres";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Erreur lors du chargement des enchères: " + e.getMessage());
            return "redirect:/error";
        }
    }

    // Méthode utilitaire pour ajouter l'utilisateur connecté au modèle
    private void addConnectedUserToModel(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getName())) {
                return;
            }

            String pseudo = authentication.getName();
            try {
                UtilisateurDTO utilisateur = utilisateurService.findByPseudo(pseudo);
                if (utilisateur != null) {
                    model.addAttribute("utilisateurConnecte", utilisateur);
                }
            } catch (UtilisateurNotFoundException e) {
                System.err.println("Utilisateur connecté non trouvé dans la base de données: " + pseudo);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur au modèle: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/encheres")
    public ResponseEntity<ServiceResponse<List<Enchere>>> getAllApi() {
        ServiceResponse<List<Enchere>> serviceResponse = enchereService.getAll();
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/api/encheres/{id}")
    public ResponseEntity<ServiceResponse<Enchere>> getEnchereById(@PathVariable long id) {
        ServiceResponse<Enchere> serviceResponse = enchereService.getById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/api/encheres/article/{articleId}")
    public ResponseEntity<ServiceResponse<Enchere>> getEnchereByArticle(@PathVariable Long articleId) {
        Article article = new Article();
        article.setNoArticle(articleId);
        ServiceResponse<Enchere> serviceResponse = enchereService.getByArticleCible(article);
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/api/encheres")
    public ResponseEntity<ServiceResponse<Enchere>> placerEnchere(
            @RequestParam Long idArticle,
            @RequestParam Long idUtilisateur,
            @RequestParam int montant) {
        ServiceResponse<Enchere> serviceResponse = enchereService.placerEnchere(idArticle, idUtilisateur, montant);
        return ResponseEntity.ok(serviceResponse);
    }

    @DeleteMapping("/api/encheres/{idEnchere}")
    public ResponseEntity<ServiceResponse<Enchere>> supprimerEnchere(@PathVariable Long idEnchere) {
        ServiceResponse<Enchere> serviceResponse = enchereService.supprimerEnchere(idEnchere);
        return ResponseEntity.ok(serviceResponse);
    }

    // Page d'erreur personnalisée pour ce contrôleur
    @GetMapping("/encheres/error")
    public String enchereError(Model model) {
        model.addAttribute("message", "Une erreur s'est produite lors du traitement des enchères");
        return "error";
    }
}