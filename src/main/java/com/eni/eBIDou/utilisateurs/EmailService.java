package com.eni.eBIDou.utilisateurs;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void envoyerLienReset(String destinataire, String lien) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setFrom("20b555604e-270e8c+1@inbox.mailtrap.io"); // ✅ adresse factice autorisée par Mailtrap
        message.setSubject("Réinitialisation de votre mot de passe");
        message.setText("Bonjour,\n\n" +
                "Voici le lien pour réinitialiser votre mot de passe :\n" +
                lien + "\n\n" +
                "Ce lien est valable une heure.\n\n" +
                "Cordialement,\nL'équipe eBIDou");

        mailSender.send(message);
    }

}
