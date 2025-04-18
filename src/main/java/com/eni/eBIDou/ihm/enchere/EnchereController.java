package com.eni.eBIDou.ihm.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String getAll(Model model) {
        ServiceResponse<List<Enchere>> serviceResponse = enchereService.getAll();
        
        if (!"200".equals(serviceResponse.code) && !"CD_SUCCESS".equals(serviceResponse.code)) {
            model.addAttribute("encheres", List.of());
            model.addAttribute("message", serviceResponse.message);
            addConnectedUserToModel(model);
            return "encheres";
        }

        model.addAttribute("encheres", serviceResponse.data);
        addConnectedUserToModel(model);

        return "encheres";
    }

    // Méthode utilitaire pour ajouter l'utilisateur connecté au modèle
    private void addConnectedUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            return;
        }

        UtilisateurDTO utilisateur = utilisateurService.findByPseudo(authentication.getName());
        if (utilisateur != null) {
            model.addAttribute("utilisateurConnecte", utilisateur);
        }
    }

    // Conserver votre endpoint API existant
    @GetMapping("/getAll")
    public ResponseEntity<ServiceResponse<List<Enchere>>> getAllApi() {
        ServiceResponse<List<Enchere>> serviceResponse = enchereService.getAll();
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getEnchereByID")
    public ResponseEntity<ServiceResponse<Enchere>> getEnchereById(long id) {
        ServiceResponse<Enchere> serviceResponse = enchereService.getById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getEnchereByArticle")
    public ResponseEntity<ServiceResponse<List<Enchere>>> getEnchereByArticle(Article article) {
        ServiceResponse<List<Enchere>> serviceResponse = enchereService.getByArticleCible(article);
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/newEnchere")
    public ResponseEntity<ServiceResponse<Enchere>> placerEnchere(@RequestBody Long idArticle, Long idUtilisateur, int montant) {
        ServiceResponse<Enchere> serviceResponse = enchereService.placerEnchere(idArticle, idUtilisateur, montant);
        return ResponseEntity.ok(serviceResponse);

    }

    @DeleteMapping("/suprEnchere")
    public ResponseEntity<ServiceResponse<Enchere>> supprimerEnchere(@RequestBody Long idEnchere){
        ServiceResponse<Enchere> serviceResponse = enchereService.supprimerEnchere(idEnchere);
        return ResponseEntity.ok(serviceResponse);
    }


}
