package com.eni.eBIDou.utilisateurs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UtilisateurNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UtilisateurNotFoundException(Long id) {
        super("Utilisateur non trouvé avec l'identifiant: " + id);
    }

    public UtilisateurNotFoundException(String pseudo) {
        super("Utilisateur non trouvé avec le pseudo: " + pseudo);
    }

    public UtilisateurNotFoundException(UtilisateurBO utilisateur) {
        super("Utilisateur non trouvé: " + utilisateur.getPseudo());
    }
}
