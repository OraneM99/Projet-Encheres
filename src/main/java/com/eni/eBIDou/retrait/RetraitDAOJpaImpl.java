package com.eni.eBIDou.retrait;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public class RetraitDAOJpaImpl implements RetraitIDAO{
    private final RetraitRepository repository;

    public RetraitDAOJpaImpl(RetraitRepository repository) {
        this.repository = repository;

    }

    @Override
    public List<Retrait> selectAll() {
        return repository.findAll();
    }

    @Override
    public Retrait selectById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Retrait findByArticleId(long idArticle) {
        return repository.findRetraitByArticle_NoArticle(idArticle);
    }

    @Override
    public List<Retrait> findByVille(String ville) {
        return repository.findByVille(ville);
    }

    @Override
    public List<Retrait> findByCodePostal(String codePostal) {
        return repository.findByCodePostal(codePostal);
    }

    @Override
    public void ajouterRetrait(Retrait retrait) {
        repository.save(retrait);

    }

    @Override
    public void supprimerRetrait(long idRetrait) {
        repository.deleteById(idRetrait);
    }
}
