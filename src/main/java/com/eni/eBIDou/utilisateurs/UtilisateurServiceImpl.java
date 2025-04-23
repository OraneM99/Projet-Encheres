package com.eni.eBIDou.utilisateurs;

import com.eni.eBIDou.Security.ResetPasswordToken;
import com.eni.eBIDou.Security.ResetPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("utilisateurServiceImpl")
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService, UserDetailsService {

    private final UtilisateurRepository repository;
    private final UtilisateurMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UtilisateurRepository utilisateurRepository;
    private final ResetPasswordTokenRepository tokenRepository;
    private final EmailService emailService;





    @Override
    public List<UtilisateurDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public UtilisateurDTO findById(Long id) {
        UtilisateurBO bo = repository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(id));
        return mapper.toDto(bo);
    }

    @Override
    public UtilisateurDTO findByPseudo(String pseudo) {
        try {
            UtilisateurBO utilisateur = repository.findByPseudo(pseudo)
                    .orElse(null);

            if (utilisateur == null) {
                System.err.println("Aucun utilisateur trouvé avec le pseudo: " + pseudo);
                return null;
            }

            return mapper.toDto(utilisateur);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par pseudo: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public boolean pseudoExiste(String pseudo) {
        return utilisateurRepository.existsByPseudo(pseudo);
    }
    
    @Override
    public boolean emailExiste(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
    
    @Override
    public UtilisateurDTO create(UtilisateurDTO dto) {
        UtilisateurBO bo = mapper.toBo(dto);
        bo.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse())); // mot de passe fourni par l'utilisateur
        bo.setActif(true);
        return mapper.toDto(repository.save(bo));
    }

    @Override
    public boolean verifierMotDePasse(Long id, String motDePasse) {
        UtilisateurBO utilisateur = repository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(id));

        return passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse());
    }

    @Override
    public UtilisateurDTO updateCredit(Long id, int newCredit) {
        UtilisateurBO existing = repository.findById(id).orElseThrow(() -> new UtilisateurNotFoundException(id));

        existing.setCredit(newCredit);
        return mapper.toDto(repository.save(existing));
    }

    @Override
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        UtilisateurBO existing = repository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(id));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setTelephone(dto.getTelephone());
        existing.setRue(dto.getRue());
        existing.setCodePostal(dto.getCodePostal());
        existing.setVille(dto.getVille());
        existing.setAdministrateur(dto.isAdministrateur());
        existing.setActif(dto.isActif());

        // Optionnel : mise à jour du mot de passe uniquement s'il est fourni
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
            existing.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        return mapper.toDto(repository.save(existing));
    }

    @Override
    public UtilisateurDTO updateAvecVerification(Long id, UtilisateurDTO dto, String motDePasseActuel) {
        if (motDePasseActuel == null || motDePasseActuel.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe actuel est requis");
        }

        if (!verifierMotDePasse(id, motDePasseActuel)) {
            throw new IllegalArgumentException("Le mot de passe actuel est incorrect");
        }

        return update(id, dto);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new UtilisateurNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public UtilisateurDTO login(String login, String motDePasse) {
        UtilisateurBO utilisateur = repository.findByEmailOrPseudo(login, login)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec login : " + login));

        if (!passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return mapper.toDto(utilisateur);
    }

    @Transactional
    public void toggleActivation(long id) {
        UtilisateurBO user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur introuvable"));
        user.setActif(!user.isActif());
        utilisateurRepository.save(user);
    }

    @Transactional
    @Override
    public void demanderReinitialisation(String email) {
        Optional<UtilisateurBO> optUtilisateur = repository.findByEmailOrPseudo(email, email);
        if (optUtilisateur.isEmpty()) {
            throw new UtilisateurNotFoundException("Aucun utilisateur trouvé avec cet email");
        }

        UtilisateurBO utilisateur = optUtilisateur.get();

        // ✅ Supprimer tout token existant pour cet utilisateur
        tokenRepository.findAll().stream()
                .filter(t -> t.getUtilisateur().getNoUtilisateur().equals(utilisateur.getNoUtilisateur()))
                .forEach(tokenRepository::delete);

        tokenRepository.flush();

        // ✅ Générer un nouveau token
        String token = UUID.randomUUID().toString();
        ResetPasswordToken resetToken = new ResetPasswordToken(
                token,
                utilisateur,
                LocalDateTime.now().plusHours(1)
        );

        tokenRepository.save(resetToken);

        // ✅ Afficher le lien en console
        String lienReset = "http://localhost:8080/reinitialiser-mot-de-passe?token=" + token;
        emailService.envoyerLienReset(utilisateur.getEmail(), lienReset);

        System.out.println("🔐 Lien de réinitialisation généré pour " + utilisateur.getEmail() + " : " + lienReset);
    }


    @Override
    public void reinitialiserMotDePasse(String token, String nouveauMotDePasse) {

        ResetPasswordToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token expiré");
        }

        UtilisateurBO utilisateur = resetToken.getUtilisateur();

        // ✅ Encodage du nouveau mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        repository.save(utilisateur);

        System.out.println("🔐 Mot de passe mis à jour pour : " + utilisateur.getEmail());

        // ✅ Nettoyage du token utilisé
        tokenRepository.delete(resetToken);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UtilisateurBO utilisateur = repository.findByEmailOrPseudo(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        return new CustomUserDetails(utilisateur);
    }
}
