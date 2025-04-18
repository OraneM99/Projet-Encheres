package com.eni.eBIDou.exceptions;

import com.eni.eBIDou.utilisateurs.UtilisateurNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Gère les exceptions génériques
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
        System.err.println("Erreur non gérée: " + e.getMessage());
        e.printStackTrace();

        // Message d'erreur simplifié pour l'utilisateur
        model.addAttribute("message", "Une erreur inattendue s'est produite. Veuillez réessayer plus tard.");
        model.addAttribute("errorDetails", e.getMessage());
        return "error";
    }

    // Gère les erreurs 404 (page non trouvée)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException e, Model model) {
        model.addAttribute("message", "La page demandée n'existe pas.");
        return "error";
    }

    // Gère les exceptions liées aux utilisateurs
    @ExceptionHandler(UtilisateurNotFoundException.class)
    public String handleUserNotFound(UtilisateurNotFoundException e, Model model) {
        model.addAttribute("message", "Utilisateur non trouvé: " + e.getMessage());
        return "error";
    }
}