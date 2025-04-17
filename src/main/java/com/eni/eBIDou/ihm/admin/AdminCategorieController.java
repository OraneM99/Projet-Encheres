package com.eni.eBIDou.ihm.admin;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.categorie.CategorieIDAO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategorieController {

    private final CategorieIDAO categorieDAO;

    public AdminCategorieController(CategorieIDAO categorieDAO) {
        this.categorieDAO = categorieDAO;
    }

    @GetMapping("/list")
    public String listCategories(Model model) {
        model.addAttribute("categories", categorieDAO.selectAll());
        return "admin/categories/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("categorie", new Categorie());
        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String addCategorie(@ModelAttribute Categorie categorie) {
        categorieDAO.ajouterCategorie(categorie);
        return "redirect:/admin/categories/list";

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Categorie categorie = categorieDAO.selectById(id);
        model.addAttribute("categorie", categorie);
        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String updateCategorie(@ModelAttribute Categorie categorie) {
        categorieDAO.modifierCategorie(categorie);
        return "redirect:/admin/categories/list";

    }

    @GetMapping("/delete/{id}")
    public String deleteCategorie(@PathVariable int id) {
        categorieDAO.supprCategorie(id);
        return "redirect:/admin/categories/list";

    }
}
