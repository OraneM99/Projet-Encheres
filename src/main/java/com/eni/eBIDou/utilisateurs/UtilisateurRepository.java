package com.eni.eBIDou.utilisateurs;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<UtilisateurBO, Long> {
    Optional<UtilisateurBO> findByPseudo(String pseudo);
    boolean existsByPseudo(String pseudo);
    boolean existsByEmail(String email);
}
