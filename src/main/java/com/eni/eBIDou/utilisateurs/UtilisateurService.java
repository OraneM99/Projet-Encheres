package com.eni.eBIDou.utilisateurs;

import java.util.List;

public interface UtilisateurService {
    List<UtilisateurDTO> findAll();
    UtilisateurDTO findById(Long id);
    UtilisateurDTO create(UtilisateurDTO dto);
    UtilisateurDTO update(Long id, UtilisateurDTO dto);
    void delete(Long id);

    UtilisateurDTO login(String login, String motDePasse);
}
