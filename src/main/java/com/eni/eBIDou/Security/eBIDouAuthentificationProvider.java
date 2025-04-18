package com.eni.eBIDou.Security;

import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import com.eni.eBIDou.utilisateurs.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class eBIDouAuthentificationProvider implements AuthenticationProvider {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginOrEmail = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Recherche de l'utilisateur par pseudo ou email
        Optional<UtilisateurBO> utilisateurOpt = utilisateurRepository.findByEmailOrPseudo(loginOrEmail, loginOrEmail);

        if (utilisateurOpt.isEmpty()) {
            throw new BadCredentialsException("Utilisateur non trouvé : " + loginOrEmail);
        }

        UtilisateurBO utilisateur = utilisateurOpt.get();


        // ✅ Vérifier si le compte est actif
        if (!utilisateur.isActif()) {
            throw new DisabledException("Le compte est désactivé.");
        }

        // ✅ Vérifier le mot de passe
        if (!passwordEncoder.matches(password, utilisateur.getMotDePasse())) {
            throw new BadCredentialsException("Mot de passe incorrect");
        }

        // ✅ Création des rôles
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (utilisateur.isAdministrateur()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }


        return new UsernamePasswordAuthenticationToken(
                utilisateur,
                null,
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
