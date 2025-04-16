package com.eni.eBIDou.ihm.retrait;

import com.eni.eBIDou.retrait.Retrait;
import com.eni.eBIDou.retrait.RetraitService;
import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RetraitController {
    private final RetraitService retraitService;

    public RetraitController(RetraitService retraitService) {
        this.retraitService = retraitService;
    }

    @GetMapping("/retraitByArticle")
    public ResponseEntity<ServiceResponse<Retrait>> retraitByArticle(@RequestParam long id){
        ServiceResponse<Retrait> serviceResponse = retraitService.selectByArticleId(id);

        return ResponseEntity.ok(serviceResponse);

    }


    @GetMapping("/retraitByVille")
    public ResponseEntity<ServiceResponse<List<Retrait>>> retraitByVille(@RequestParam String ville){
        ServiceResponse<List<Retrait>> serviceResponse = retraitService.selectByVille(ville);

        return ResponseEntity.ok(serviceResponse);

    }

    @GetMapping("/retraitByCP")
    public ResponseEntity<ServiceResponse<List<Retrait>>> retraitByCodePostal(@RequestParam String codePostal){
        ServiceResponse<List<Retrait>> serviceResponse = retraitService.selectByCodePostal(codePostal);

        return ResponseEntity.ok(serviceResponse);

    }


    @PostMapping("/ajoutRetrait")
    public  ResponseEntity<ServiceResponse<Retrait>> ajoutRetrait(@RequestBody Retrait retrait){
        ServiceResponse<Retrait> serviceResponse = retraitService.ajouterRetrait(retrait);
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/supprRetrait")
    public ResponseEntity<ServiceResponse<Retrait>> supprimerRetrait(@RequestBody long id){
        ServiceResponse<Retrait> serviceResponse = retraitService.supprimerRetrait(id);
        return ResponseEntity.ok(serviceResponse);
    }

}
