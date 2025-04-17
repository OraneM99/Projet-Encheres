package com.eni.eBIDou.utilisateurs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<UtilisateurBO, Long> {
    Optional<UtilisateurBO> findByPseudo(String pseudo);
    Optional<UtilisateurBO> findByEmailOrPseudo(String email, String pseudo);
    boolean existsByPseudo(String pseudo);
    boolean existsByEmail(String email);
}
