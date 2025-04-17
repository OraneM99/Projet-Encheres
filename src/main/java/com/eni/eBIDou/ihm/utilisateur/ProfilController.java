package com.eni.eBIDou.ihm.utilisateur;


import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurDTO;
import com.eni.eBIDou.utilisateurs.UtilisateurMapper;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilController {

    private UtilisateurService utilisateurService;
    private UtilisateurMapper utilisateurMapper;

    public ProfilController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {
        this.utilisateurService = utilisateurService;
        this.utilisateurMapper = utilisateurMapper;
    }

    @GetMapping("/profil")
    public String afficherProfil(Model model, @AuthenticationPrincipal UtilisateurBO utilisateurConnecte) {

        model.addAttribute("utilisateurDTO", utilisateurMapper.toDto(utilisateurConnecte));

        return "profil";
    }



}
