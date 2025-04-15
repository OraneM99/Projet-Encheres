package com.eni.eBIDou.utilisateurs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UtilisateurNotFoundException extends RuntimeException {
    public UtilisateurNotFoundException(Long id) {
        super("Utilisateur non trouvé avec l'ID : " + id);
    }
}
