package com.eni.eBIDou.Security;

import com.eni.eBIDou.utilisateurs.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/login")
    public String loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() &&
                !(auth.getPrincipal() instanceof String && "anonymousUser".equals(auth.getPrincipal()))) {
            return "redirect:/accueil";
        }
        return "login";
    }

    @GetMapping("/mot-de-passe-oublie")
    public String afficherFormulaireMotDePasseOublie() {
        return "form-mot-de-passe-oublie";
    }

    @PostMapping("/mot-de-passe-oublie")
    public String traiterDemande(@RequestParam String email, RedirectAttributes redirectAttributes) {
        utilisateurService.demanderReinitialisation(email);
        redirectAttributes.addFlashAttribute("message", "Lien affiché en console !");
        return "redirect:/login";
    }

    @GetMapping("/reinitialiser-mot-de-passe")
    public String afficherFormulaireReset(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "form-reset-mdp";
    }

    @PostMapping("/reinitialiser-mot-de-passe")
    public String traiterReset(@RequestParam String token,
                               @RequestParam String motDePasse,
                               RedirectAttributes redirectAttributes) {
        System.out.println("🔥 appel reinitialiserMotDePasse avec token = " + token);
        utilisateurService.reinitialiserMotDePasse(token, motDePasse);
        redirectAttributes.addFlashAttribute("message", "Mot de passe modifié avec succès !");
        return "redirect:/login";
    }
}
