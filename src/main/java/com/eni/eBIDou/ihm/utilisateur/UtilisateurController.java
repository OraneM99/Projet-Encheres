package com.eni.eBIDou.ihm.utilisateur;

import com.eni.eBIDou.utilisateurs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final UtilisateurMapper mapper;


    private UtilisateurDTO getConnectedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return utilisateurService.findById(userDetails.getUtilisateur().getNoUtilisateur());
        }

        return null;
    }


    // Affiche le profil de l'utilisateur connecté
    @GetMapping("/profil")
    public String afficherMonProfil(Model model) {
        UtilisateurDTO utilisateurDTO = getConnectedUser();
        if (utilisateurDTO == null) {
            return "redirect:/login";
        }

        model.addAttribute("utilisateurDTO", utilisateurDTO);
        model.addAttribute("isSelfProfile", true);
        return "profil";
    }

    // Affiche la page de modification du profil
    @GetMapping("/modifier-profil")
    public String afficherPageModification(Model model) {
        UtilisateurDTO utilisateurDTO = getConnectedUser();
        if (utilisateurDTO == null) {
            return "redirect:/login";
        }

        model.addAttribute("utilisateurDTO", utilisateurDTO);
        return "modifier-profil";
    }

    // Traite les modifications du profil
    @PostMapping("/profil")
    public String modifierProfil(@ModelAttribute UtilisateurDTO utilisateurDTO,
                                 @RequestParam String motDePasseActuel,
                                 @RequestParam(required = false) String confirmation,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Vérification que le nouveau mot de passe et la confirmation correspondent
            if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty() &&
                    !utilisateurDTO.getMotDePasse().equals(confirmation)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Le nouveau mot de passe et la confirmation ne correspondent pas");
                return "redirect:/modifier-profil";
            }

            // Utilisation de la méthode avec vérification du mot de passe actuel
            utilisateurService.updateAvecVerification(utilisateurDTO.getNoUtilisateur(), utilisateurDTO, motDePasseActuel);

            redirectAttributes.addFlashAttribute("successMessage", "Votre profil a été mis à jour avec succès");
            return "redirect:/profil";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/modifier-profil";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la mise à jour du profil: " + e.getMessage());
            return "redirect:/modifier-profil";
        }
    }

    // Supprime le compte utilisateur
    @PostMapping("/profil/supprimer/{id}")
    public String supprimerCompte(@PathVariable Long id) {
        utilisateurService.delete(id);
        return "redirect:/logout";
    }

    // Affiche le profil d'un utilisateur par son pseudo (visible par les autres)
    @GetMapping("/utilisateurs/{pseudo}")
    public String afficherProfilUtilisateur(@PathVariable String pseudo, Model model) {
        UtilisateurDTO utilisateurDTO = utilisateurService.findByPseudo(pseudo);

        if (utilisateurDTO == null) {
            model.addAttribute("message", "Utilisateur non trouvé");
            return "error";
        }
        
        boolean isSelfProfile = false;
        UtilisateurDTO connectedUser = getConnectedUser();
        if (connectedUser != null) {
            isSelfProfile = pseudo.equals(connectedUser.getPseudo());
        }

        model.addAttribute("utilisateurDTO", utilisateurDTO);
        model.addAttribute("isSelfProfile", isSelfProfile);
        return "profil";
    }

    @PostMapping("/crediter")
    public String crediterBIDcoins(@RequestParam int montant, RedirectAttributes redirectAttributes) {
        UtilisateurDTO utilisateurDTO = getConnectedUser();
        if (utilisateurDTO == null) {
            return "redirect:/login";
        }

        // 💰 Met à jour le crédit
        UtilisateurDTO updated = utilisateurService.updateCredit(
                utilisateurDTO.getNoUtilisateur(),
                utilisateurDTO.getCredit() + montant
        );

        // 🔁 Rafraîchir le SecurityContext avec les nouveaux crédits
        UtilisateurBO utilisateurBO = mapper.toBo(updated);
        CustomUserDetails updatedDetails = new CustomUserDetails(utilisateurBO);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedDetails,
                null,
                updatedDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        redirectAttributes.addFlashAttribute("successMessage", montant + " BIDcoins ajoutés à votre compte.");
        return "redirect:/utilisateurs/profil";
    }






}