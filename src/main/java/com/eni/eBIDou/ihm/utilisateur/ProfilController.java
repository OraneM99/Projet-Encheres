package com.eni.eBIDou.ihm.utilisateur;


import com.eni.eBIDou.utilisateurs.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ProfilController {

    private UtilisateurService utilisateurService;
    private UtilisateurMapper utilisateurMapper;

    public ProfilController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {
        this.utilisateurService = utilisateurService;
        this.utilisateurMapper = utilisateurMapper;
    }

    @GetMapping("/profil")
    public String afficherProfil(Model model) {
        // Ajouter l'utilisateur connecté au modèle
        return "profil";
    }

    @GetMapping("/profil/{id}")
    public String afficherProfilParId(@PathVariable Long id, Model model) {
        UtilisateurDTO utilisateur = utilisateurService.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "profil-autre";
    }

    @GetMapping("/modifier-profil")
    public String afficherFormulaireModification(Model model) {
        return "modifier-profil";
    }

    @PostMapping("/modifier-profil")
    public String modifierProfil(@ModelAttribute UtilisateurDTO utilisateurDTO,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 RedirectAttributes redirectAttributes) {
        long idUtilisateurAmodifier = customUserDetails.getUtilisateur().getNoUtilisateur();
        utilisateurService.update(idUtilisateurAmodifier, utilisateurDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Votre profil a été mis à jour avec succès");
        return "redirect:/profil";
    }

    @PostMapping("/profil/supprimer/{id}")
    public String supprimerProfil(@PathVariable long id,
                                  @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        if (customUserDetails.getUtilisateur().getNoUtilisateur() != id) {
            System.out.println("Erreur : tentative de suppression d'un autre compte.");
            throw new AccessDeniedException("Action non autorisée");
        }

        utilisateurService.delete(id);
        new SecurityContextLogoutHandler().logout(request, response, null);
        return "redirect:/accueil";
    }
}
