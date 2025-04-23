package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.article.ArticleFormDTO;
import com.eni.eBIDou.images.AzureBlobStorageService;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.retrait.RetraitService;
import com.eni.eBIDou.service.ServiceResponse;
import com.eni.eBIDou.utilisateurs.CustomUserDetails;
import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class VenteController {

    private final CategorieService categorieService;
    private final ArticleService articleService;
    private final UtilisateurMapper utilisateurMapper;
    private final RetraitService retraitService;
    private final AzureBlobStorageService azureBlobStorageService;

    public VenteController(CategorieService categorieService,
                           ArticleService articleService,
                           UtilisateurMapper utilisateurMapper,
                           RetraitService retraitService,
                           AzureBlobStorageService azureBlobStorageService) {
        this.categorieService = categorieService;
        this.articleService = articleService;
        this.utilisateurMapper = utilisateurMapper;
        this.retraitService = retraitService;
        this.azureBlobStorageService = azureBlobStorageService;
    }


    @GetMapping("/nouvelle-vente")
    public String afficherNewVentes(Model model) {

        ArticleFormDTO articleForm = new ArticleFormDTO();
        articleForm.setArticle(new Article());
        articleForm.setRetrait(new Retrait());

        model.addAttribute("articleForm", articleForm);

        //récupérer la liste des categories
        List<Categorie> categories = categorieService.selectAll().getData();
        model.addAttribute("categories", categories);

        return "nouvelle-vente";
    }

    @PostMapping("/creer")
    public String creerVente(@ModelAttribute ArticleFormDTO articleForm,
                             @RequestParam("image") MultipartFile image,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Article article = articleForm.getArticle();
        Retrait retrait = articleForm.getRetrait();

        // Lier l'article à l'utilisateur connecté
        article.setVendeur(customUserDetails.getUtilisateur());

        // 🖼️ Gérer l'upload de l'image
        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = azureBlobStorageService.uploadFile(image);
                article.setUrlImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                // tu peux ajouter un message d'erreur dans le modèle ici si besoin
            }
        }

        // Sauvegarder l'article
        ServiceResponse<Article> articleSauvegarde = articleService.addArticle(article);

        // Associer et enregistrer le retrait
        retrait.setArticle(articleSauvegarde.getData());
        retraitService.ajouterRetrait(retrait);

        return "redirect:/accueil";
    }

}