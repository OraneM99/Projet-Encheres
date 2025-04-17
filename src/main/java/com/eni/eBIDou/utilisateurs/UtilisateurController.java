package com.eni.eBIDou.utilisateurs;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UtilisateurDTO> findAll() {
        return service.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UtilisateurDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> create(@RequestBody @Valid UtilisateurDTO dto) {
        UtilisateurDTO saved = service.create(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public UtilisateurDTO update(@PathVariable Long id, @RequestBody @Valid UtilisateurDTO dto) {
        return service.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UtilisateurDTO> login(@RequestParam String login, @RequestParam String motDePasse) {
        UtilisateurDTO utilisateur = service.login(login, motDePasse);
        return ResponseEntity.ok(utilisateur);
    }
}
