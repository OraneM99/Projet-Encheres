package com.eni.eBIDou.utilisateurs;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("mock")
@Primary
public class UtilisateurRepositoryMock implements UtilisateurRepository {

    private final Map<Long, UtilisateurBO> utilisateurs = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UtilisateurRepositoryMock() {
        // Données de test
        save(new UtilisateurBO(null, "toto", "Martin", "Paul", "paul.martin@mail.com", "0101010101", "1 rue de Paris", "75000", "Paris", 100, false, "pass"));
        save(new UtilisateurBO(null, "tata", "Dupont", "Sophie", "sophie.dupont@mail.com", "0606060606", "2 avenue Victor Hugo", "69000", "Lyon", 150, false, "pass"));
        save(new UtilisateurBO(null, "admin", "Root", "Admin", "admin@mail.com", "0505050505", "3 boulevard Haussmann", "75009", "Paris", 1000, true, "pass"));
        save(new UtilisateurBO(null, "bidou", "Bidouille", "Test", "bidou@mail.com", "0707070707", "4 place Bellecour", "69002", "Lyon", 200, false, "pass"));
        save(new UtilisateurBO(null, "rudyG", "Guillaume", "Rudy", "rudy@mail.com", "0601020304", "10 chemin des Bonbons", "59300", "Valenciennes", 500, false, "pass"));
        save(new UtilisateurBO(null, "superlongpseudo", "Super", "LongPseudo", "superlongpseudo@mail.com", "0600000000", "11 rue Loooong", "34000", "Montpellier", 300, false, "pass"));
        save(new UtilisateurBO(null, "johnDoe", "Doe", "John", "john.doe@mail.com", null, null, null, null, 50, false, "pass"));
        save(new UtilisateurBO(null, "janeDoe", "Doe", "Jane", "jane.doe@mail.com", null, null, null, null, 75, false, "pass"));
        save(new UtilisateurBO(null, "toto", "Toto", "Tata", "toto@mail.com", null, null, null, null, 100, false, "pass"));
        save(new UtilisateurBO(null, "tata", "Tata", "Titi", "tata@mail.com", null, null, null, null, 100, false, "pass"));
    }

    @Override
    public List<UtilisateurBO> findAll() {
        return new ArrayList<>(utilisateurs.values());
    }

    @Override
    public Optional<UtilisateurBO> findById(Long id) {
        return Optional.ofNullable(utilisateurs.get(id));
    }

    @Override
    public Optional<UtilisateurBO> findByPseudo(String pseudo) {
        return utilisateurs.values().stream()
                .filter(u -> u.getPseudo().equalsIgnoreCase(pseudo))
                .findFirst();
    }

    @Override
    public Optional<UtilisateurBO> findByEmailOrPseudo(String email, String pseudo) {
        return utilisateurs.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) || u.getPseudo().equalsIgnoreCase(pseudo))
                .findFirst();
    }

    @Override
    public boolean existsByPseudo(String pseudo) {
        return utilisateurs.values().stream().anyMatch(u -> u.getPseudo().equalsIgnoreCase(pseudo));
    }

    @Override
    public boolean existsByEmail(String email) {
        return utilisateurs.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean existsById(Long id) {
        return utilisateurs.containsKey(id);
    }

    @Override
    public UtilisateurBO save(UtilisateurBO utilisateur) {
        if (utilisateur.getNoUtilisateur() == null) {
            utilisateur.setNoUtilisateur(idGenerator.getAndIncrement());
        }
        utilisateurs.put(utilisateur.getNoUtilisateur(), utilisateur);
        return utilisateur;
    }

    @Override
    public void deleteById(Long id) {
        utilisateurs.remove(id);
    }
}
