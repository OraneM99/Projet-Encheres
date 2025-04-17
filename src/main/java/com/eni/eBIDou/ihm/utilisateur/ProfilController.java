package com.eni.eBIDou.ihm.utilisateur;


import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurMapper;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String afficherProfil(Model model, @RequestParam(value="edit", required = false) Boolean editMode, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte) {

        //recuperer l'utilisateur Connecte
        model.addAttribute("utilisateurDTO", utilisateurMapper.toDto(utilisateurConnecte));

        //pour gérer l'action d'appui sur le bouton modifier
        model.addAttribute("editMode", Boolean.TRUE.equals(editMode));
        return "profil";
    }

    @PostMapping("/profil")
    public String modifierProfil(@ModelAttribute UtilisateurDTO utilisateurDTO, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte) {
        //récupère l'id utilisateur du profil connecte
        long idUtilisateurAmodifier = utilisateurConnecte.getNoUtilisateur();

        //envoie la modification avec l'id du profilCo' et le form utilisateurDTO avec les nouvelles valeurs
        utilisateurService.update(idUtilisateurAmodifier, utilisateurDTO);

        return "redirect:/profil";

    }

    @PostMapping("/profil/supprimer/{id}")
    public String supprimerProfil(@PathVariable long id, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte, HttpServletRequest request, HttpServletResponse response) {

        // empêcher de supprimer un autre compte que le sien
        if (utilisateurConnecte.getNoUtilisateur() != id) {
            System.out.println("Erreur : tentative de suppression d'un autre compte.");
            throw new AccessDeniedException("Action non autorisée");
        }
        utilisateurService.delete(id);
        // forcer la déconnexion
        new SecurityContextLogoutHandler().logout(request, response, null);

        // Redirection vers une page d'accueil
        return "redirect:/accueil";
    }




}
