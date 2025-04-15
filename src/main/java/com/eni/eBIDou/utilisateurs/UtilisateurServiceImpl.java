package com.eni.eBIDou.utilisateurs;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository repository;
    private final UtilisateurMapper mapper;

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
    public UtilisateurDTO create(UtilisateurDTO dto) {
        UtilisateurBO bo = mapper.toBo(dto);
        bo.setMotDePasse(new BCryptPasswordEncoder().encode("passwordParDefaut")); // mot de passe à changer après
        return mapper.toDto(repository.save(bo));
    }

    @Override
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        UtilisateurBO bo = repository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(id));

        bo.setNom(dto.getNom());
        bo.setPrenom(dto.getPrenom());
        bo.setEmail(dto.getEmail());
        bo.setTelephone(dto.getTelephone());
        bo.setRue(dto.getRue());
        bo.setCodePostal(dto.getCodePostal());
        bo.setVille(dto.getVille());
        bo.setCredit(dto.getCredit());
        bo.setAdministrateur(dto.isAdministrateur());

        return mapper.toDto(repository.save(bo));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new UtilisateurNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
