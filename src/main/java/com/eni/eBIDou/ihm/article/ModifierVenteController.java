package com.eni.eBIDou.ihm.article;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleFormDTO;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieService;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.retrait.RetraitService;
import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ModifierVenteController {

    private final CategorieService categorieService;
    private final ArticleService articleService;
    private final RetraitService retraitService;


    public ModifierVenteController(CategorieService categorieService, ArticleService articleService, RetraitService retraitService) {
        this.categorieService = categorieService;
        this.articleService = articleService;
        this.retraitService = retraitService;

    }

    @GetMapping("/articles/modifier/{id}")
    public String showModifierVente(@PathVariable("id") long id, Model model) {
        //Récupérer l'article
        ServiceResponse<Article> article = articleService.getArticleById(id);
        Article articleAVendre = article.getData();

        if(articleAVendre.getEtatVente() != EtatVente.CREEE){
            return "redirect:/detailVente/" + id;
        }


        ServiceResponse<Retrait> retraitData = retraitService.selectByArticleId(articleAVendre.getNoArticle());
        Retrait retrait = retraitData.getData();


        ArticleFormDTO articleForm = new ArticleFormDTO();
        articleForm.setArticle(articleAVendre);
        articleForm.setRetrait(retrait);


        // Récupérer la liste des categories
        List<Categorie> categories = categorieService.selectAll().getData();

        model.addAttribute("categories", categories);
        model.addAttribute("articleForm", articleForm);

        return "modifier-vente";


    }

    @PostMapping("/modifierVente")
    public String modifierVente(@ModelAttribute(name = "articleForm") ArticleFormDTO articleForm, RedirectAttributes redirectAttributes) {

        Article articleModif = articleForm.getArticle();

        redirectAttributes.addFlashAttribute("successMessage", "Votre vente a été mise à jour avec succès");
        return "redirect:/detailVente/" + articleModif.getNoArticle();
    }



}
