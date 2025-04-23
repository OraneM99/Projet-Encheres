package com.eni.eBIDou.utilisateurs;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    List<UtilisateurDTO> findAll();

    boolean pseudoExiste(String pseudo);

    boolean emailExiste(String email);
    
    void toggleActivation(long id);
    
    UtilisateurDTO findById(Long id);

    UtilisateurDTO findByPseudo(String pseudo);

    UtilisateurDTO create(UtilisateurDTO dto);

    boolean verifierMotDePasse(Long id, String motDePasse);

    UtilisateurDTO update(Long id, UtilisateurDTO dto);

    UtilisateurDTO updateCredit(Long id, int newCredit);

    UtilisateurDTO updateAvecVerification(Long id, UtilisateurDTO dto, String motDePasseActuel);

    void delete(Long id);

    UtilisateurDTO login(String login, String motDePasse);

    void reinitialiserMotDePasse(String token, String nouveauMotDePasse);

    void demanderReinitialisation(String email);


}
