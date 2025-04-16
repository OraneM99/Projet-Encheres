package com.eni.eBIDou.Security;

import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class eBIDouAuthentificationProvider implements AuthenticationProvider {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginOrEmail = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("Tentative de connexion avec : " + loginOrEmail);

        // Recherche de l'utilisateur par pseudo ou email
        Optional<UtilisateurBO> utilisateurOpt = utilisateurRepository.findByEmailOrPseudo(loginOrEmail, loginOrEmail);

        if (utilisateurOpt.isEmpty()) {
            throw new BadCredentialsException("Utilisateur non trouvé : " + loginOrEmail);
        }

        UtilisateurBO utilisateur = utilisateurOpt.get();

        System.out.println("Utilisateur trouvé : " + utilisateur.getPseudo());
        System.out.println("Mot de passe fourni : " + password);
        System.out.println("Mot de passe stocké : " + utilisateur.getMotDePasse());

        // Vérifier le mot de passe 
        if (!password.equals(utilisateur.getMotDePasse())) {
            throw new BadCredentialsException("Mot de passe incorrect");
        }

        // Création des autorités/rôles
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (utilisateur.isAdministrateur()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        System.out.println("Connexion réussie pour : " + utilisateur.getPseudo());
        
        return new UsernamePasswordAuthenticationToken(
                utilisateur.getPseudo(), 
                null,                   
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}