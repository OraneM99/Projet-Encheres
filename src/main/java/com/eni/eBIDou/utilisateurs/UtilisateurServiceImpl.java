package com.eni.eBIDou.utilisateurs;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository repository;
    private final UtilisateurMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UtilisateurRepository utilisateurRepository;

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
        return mapper.toDto(repository.save(bo));
    }

    @Override
    public boolean verifierMotDePasse(Long id, String motDePasse) {
        UtilisateurBO utilisateur = repository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(id));

        return passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse());
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

}
