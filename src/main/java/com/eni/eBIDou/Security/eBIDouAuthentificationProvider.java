package com.eni.eBIDou.Security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class eBIDouAuthentificationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Exemple en dur - à remplacer par un appel à un service ou une BDD
        if ("test@test.com".equals(email) && "secret".equals(password)) {

            System.out.println("Connexion OK");
            return new UsernamePasswordAuthenticationToken(email, password, List.of());
        }

        throw new BadCredentialsException("Identifiants invalides");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

