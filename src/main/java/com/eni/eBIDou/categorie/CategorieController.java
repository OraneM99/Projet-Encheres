package com.eni.eBIDou.categorie;

import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Profile("jpa")
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;

    }


    @GetMapping("/getAllCategories")
    public ResponseEntity<ServiceResponse<List<Categorie>>> getAllCategories() {
        //Appeler le Métier
        ServiceResponse<List<Categorie>> serviceResponse = categorieService.selectAll();

        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getCategorieByID")
    public ResponseEntity<ServiceResponse<Categorie>> getCategorieById(long id) {
        ServiceResponse<Categorie> serviceResponse = categorieService.selectById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getCategorieByName")
    public ResponseEntity<ServiceResponse<Categorie>> getCategoriesByName(String name) {
        ServiceResponse<Categorie> serviceResponse = categorieService.selectByName(name);
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/AddCategorie")
    public ResponseEntity<ServiceResponse<Categorie>> create(@RequestBody Categorie categorie) {
        ServiceResponse<Categorie> response = categorieService.addCategorie(categorie);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/ModifCate")
    public ResponseEntity<ServiceResponse<Categorie>> modify(@RequestBody Categorie categorie) {
        ServiceResponse<Categorie> response = categorieService.updateCategorie(categorie);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/SupprCate")
    public ResponseEntity<ServiceResponse<Categorie>> delete(@RequestBody long idCategorie) {
        ServiceResponse<Categorie> response = categorieService.deleteCategorie(idCategorie);

        return ResponseEntity.ok(response);
    }

}
