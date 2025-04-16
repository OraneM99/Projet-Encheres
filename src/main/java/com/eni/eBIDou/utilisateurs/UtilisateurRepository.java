package com.eni.eBIDou.utilisateurs;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public interface UtilisateurRepository extends JpaRepository<UtilisateurBO, Long> {
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

