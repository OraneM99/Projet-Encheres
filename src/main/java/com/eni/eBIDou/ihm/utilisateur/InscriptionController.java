package com.eni.eBIDou.ihm.utilisateur;

import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InscriptionController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/register")
    public String afficherPageInscription(Model model) {
        model.addAttribute("utilisateurDTO", new UtilisateurDTO());
        return "register";
    }

    @PostMapping("/register")
    public String inscrireUtilisateur(
            @ModelAttribute("utilisateurDTO") UtilisateurDTO utilisateur,
            @RequestParam String confirmation,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Vérifications des champs obligatoires
        if (utilisateur.getPseudo() == null || utilisateur.getPseudo().isBlank()) {
            model.addAttribute("error", "Le pseudo est obligatoire");
            return "register";
        }

        if (utilisateur.getEmail() == null || utilisateur.getEmail().isBlank()) {
            model.addAttribute("error", "L'email est obligatoire");
            return "register";
        }

        // Vérification que le pseudo est unique
        if (utilisateurService.pseudoExiste(utilisateur.getPseudo())) {
            model.addAttribute("error", "Ce pseudo est déjà utilisé");
            return "register";
        }

        // Vérification que l'email est unique
        if (utilisateurService.emailExiste(utilisateur.getEmail())) {
            model.addAttribute("error", "Cet email est déjà utilisé");
            return "register";
        }

        // Vérification que les mots de passe correspondent
        if (!utilisateur.getMotDePasse().equals(confirmation)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas");
            return "register";
        }

        try {
            UtilisateurDTO createdUser = utilisateurService.create(utilisateur);

            redirectAttributes.addFlashAttribute("success", "Votre compte a été créé avec succès. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";

        } catch (Exception e) {
            // En cas d'erreur lors de la création
            model.addAttribute("error", "Une erreur est survenue lors de la création du compte : " + e.getMessage());
            return "register";
        }
    }
}
