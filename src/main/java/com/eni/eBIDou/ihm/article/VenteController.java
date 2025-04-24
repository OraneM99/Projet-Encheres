package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.article.ArticleFormDTO;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.eni.eBIDou.service.ServiceConstant.CD_SUCCESS;

@Controller
public class VenteController {

    private final CategorieService categorieService;
    private final ArticleService articleService;
    private final RetraitService retraitService;
    private final EnchereService enchereService;
    private final AzureBlobStorageService azureBlobStorageService;

    public VenteController(CategorieService categorieService,
                           ArticleService articleService,
                           RetraitService retraitService,
                           EnchereService enchereService,
                           AzureBlobStorageService azureBlobStorageService) {
        this.categorieService = categorieService;
        this.articleService = articleService;
        this.retraitService = retraitService;
        this.enchereService = enchereService;
        this.azureBlobStorageService = azureBlobStorageService;
    }


    @GetMapping("/nouvelle-vente")
    public String afficherNewVentes(Model model) {

        ArticleFormDTO articleForm = new ArticleFormDTO();
        articleForm.setArticle(new Article());
        articleForm.setRetrait(new Retrait());

        model.addAttribute("articleForm", articleForm);

        // Récupérer la liste des categories
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
            }
        }

        // Sauvegarder l'article
        ServiceResponse<Article> articleSauvegarde = articleService.addArticle(article);

        // Associer et enregistrer le retrait
        retrait.setArticle(articleSauvegarde.getData());
        retraitService.ajouterRetrait(retrait);

        return "redirect:/accueil";
    }

    @GetMapping("/vente")
    public String afficherPageVentes(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // Récupérer l'utilisateur connecté
        UtilisateurBO utilisateur = customUserDetails.getUtilisateur();
        Long noUtilisateur = utilisateur.getNoUtilisateur();

        // Récupérer tous les articles
        ServiceResponse<List<Article>> articlesResponse = articleService.getAll();
        List<Article> mesVentes = new ArrayList<>();

        // Si des articles ont été trouvés, filtrer ceux du vendeur connecté
        if (CD_SUCCESS.equals(articlesResponse.getCode()) && articlesResponse.getData() != null) {
            mesVentes = articlesResponse.getData().stream()
                    .filter(article -> article.getVendeur() != null &&
                            Objects.equals(article.getVendeur().getNoUtilisateur(), noUtilisateur))
                    .collect(Collectors.toList());
        }

        // Récupérer toutes les enchères
        ServiceResponse<List<Enchere>> encheresResponse = enchereService.getAll();
        List<Enchere> mesEncheres = new ArrayList<>();

        // Si des enchères ont été trouvées, filtrer celles de l'utilisateur connecté
        if (CD_SUCCESS.equals(encheresResponse.getCode()) && encheresResponse.getData() != null) {
            mesEncheres = encheresResponse.getData().stream()
                    .filter(enchere -> enchere.getEncherisseur() != null &&
                            Objects.equals(enchere.getEncherisseur().getNoUtilisateur(), noUtilisateur))
                    .collect(Collectors.toList());
        }

        // Ajouter les données au modèle
        model.addAttribute("titre", "Mes Ventes et Enchères");
        model.addAttribute("description", "Voici la liste de vos ventes et enchères");
        model.addAttribute("mesVentes", mesVentes);
        model.addAttribute("mesEncheres", mesEncheres);

        return "vente";
    }
}