package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/vente")
public class VenteController {
    
    private final CategorieService categorieService;
    private final UtilisateurService utilisateurService;
    private final ArticleService articleService;
    
    public VenteController(CategorieService categorieService,
                           UtilisateurService utilisateurService,
                           ArticleService articleService) {
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
        this.articleService = articleService;
    }

    @GetMapping
    public String afficherVentes(Model model) {
        List<Article> articles = articleService.getAll().getData();
        model.addAttribute("articles", articles);

        return "nouvelle-vente";
    }

//    @GetMapping("/nouvelle")
//    public String afficheFormulaireVente(Model model, Principal principal) {
//        // Récupération directe du vendeur via son pseudo
//        String pseudo = principal.getName();
//        UtilisateurBO utilisateur = utilisateurService.findByPseudo(pseudo).get();
//        
//        model.addAttribute("utilisateur", utilisateur);
//        model.addAttribute("aricle", new Article());
//        
//        // Chargement des catégories
//        ServiceResponse<List<Categorie>> responseCategorie = categorieService.selectAll();
//        model.addAttribute("categorie", responseCategorie.getData());
//        
//        return "nouvelle-vente";
//    }
    
//    @PostMapping("/creer")
//    public String creerVente(@ModelAttribute("article") Article article,
//                             @RequestParam("photoFile") MultipartFile photoFile,
//                             Principal principal,
//                             RedirectAttributes redirectAttributes) {
//        // Associer l’utilisateur vendeur
//        String pseudo = principal.getName();
//        UtilisateurBO vendeur = utilisateurService.findByPseudo(pseudo).get();
//        article.setVendeur(vendeur);
//
//        // TODO : gérer sauvegarde, image, etc.
//        redirectAttributes.addFlashAttribute("success", "Article mis en vente avec succès !");
//        return "redirect:/encheres";
//    }
}

