package com.eni.eBIDou.ihm.enchere;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.enchere.Enchere;
import com.eni.eBIDou.enchere.EnchereService;
import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@Profile("jpa")
public class EnchereController {

    private final EnchereService service;

    public EnchereController(EnchereService service) {
        this.service = service;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ServiceResponse<List<Enchere>>> getAll() {
        //Appeler le Métier
        ServiceResponse<List<Enchere>> serviceResponse = service.getAll();

        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getEnchereByID")
    public ResponseEntity<ServiceResponse<Enchere>> getEnchereById(long id) {
        ServiceResponse<Enchere> serviceResponse = service.getById(id);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/getEnchereByArticle")
    public ResponseEntity<ServiceResponse<List<Enchere>>> getEnchereByArticle(Article article) {
        ServiceResponse<List<Enchere>> serviceResponse = service.getByArticleCible(article);
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/newEnchere")
    public ResponseEntity<ServiceResponse<Enchere>> placerEnchere(@RequestBody Long idArticle, Long idUtilisateur, int montant) {
        ServiceResponse<Enchere> serviceResponse = service.placerEnchere(idArticle, idUtilisateur, montant);
        return ResponseEntity.ok(serviceResponse);

    }

    @DeleteMapping("/suprEnchere")
    public ResponseEntity<ServiceResponse<Enchere>> supprimerEnchere(@RequestBody Long idEnchere){
        ServiceResponse<Enchere> serviceResponse = service.supprimerEnchere(idEnchere);
        return ResponseEntity.ok(serviceResponse);
    }


}
