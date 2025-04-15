package com.eni.eBIDou.utilisateurs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository {
    List<UtilisateurBO> findAll();
    Optional<UtilisateurBO> findById(Long id);
    Optional<UtilisateurBO> findByPseudo(String pseudo);
    Optional<UtilisateurBO> findByEmailOrPseudo(String email, String pseudo);
    boolean existsByPseudo(String pseudo);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    UtilisateurBO save(UtilisateurBO utilisateur);
    void deleteById(Long id);
}

