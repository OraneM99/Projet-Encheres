package com.eni.eBIDou.initializer;

import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
public class UtilisateurInitializer implements DataInitializer {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurInitializer(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void initialize() {
        if (isInitialized()) {
            System.out.println("Les utilisateurs sont déjà initialisés");
            return;
        }

        // Création des utilisateurs
        UtilisateurBO admin = new UtilisateurBO();
        admin.setPseudo("admin");
        admin.setNom("Admin");
        admin.setPrenom("Admin");
        admin.setEmail("admin@ebidou.fr");
        admin.setTelephone("0123456789");
        admin.setRue("1 Rue de l'Admin");
        admin.setCodePostal("75000");
        admin.setVille("Paris");
        admin.setCredit(500);
        admin.setAdministrateur(true);
        admin.setMotDePasse(passwordEncoder.encode("admin123"));
        utilisateurRepository.save(admin);

        UtilisateurBO user1 = new UtilisateurBO();
        user1.setPseudo("user1");
        user1.setNom("Dupont");
        user1.setPrenom("Jean");
        user1.setEmail("jean.dupont@ebidou.fr");
        user1.setTelephone("0612345678");
        user1.setRue("15 Rue des Lilas");
        user1.setCodePostal("44000");
        user1.setVille("Nantes");
        user1.setCredit(200);
        user1.setAdministrateur(false);
        user1.setMotDePasse(passwordEncoder.encode("user123"));
        utilisateurRepository.save(user1);

        // Autres utilisateurs...

        System.out.println("Utilisateurs initialisés avec succès");
    }

    @Override
    public boolean isInitialized() {
        return utilisateurRepository.count() > 0;
    }

    @Override
    public String getName() {
        return "UtilisateurInitializer";
    }
}