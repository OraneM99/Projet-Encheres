package com.eni.eBIDou.vues;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminVueController {

    private final UtilisateurService utilisateurService;


    public AdminVueController(UtilisateurService utilisateurService, CategorieService categorieService, CategorieService categorieService1) {
        this.utilisateurService = utilisateurService;

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Tu peux injecter des statistiques ici si besoin
        return "admin/index"; // correspond à /templates/admin/dashboard.html
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/utilisateurs")
    public String utilisateurs(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.findAll());
        return "admin/utilisateurs/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/utilisateurs/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("utilisateur", utilisateurService.findById(id));
        return "admin/utilisateurs/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/utilisateurs/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute UtilisateurDTO utilisateur) {
        utilisateurService.update(id, utilisateur);
        return "redirect:/admin/utilisateurs";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/utilisateurs/delete/{id}")
    public String delete(@PathVariable Long id) {
        utilisateurService.delete(id);
        return "redirect:/admin/utilisateurs";
    }

}