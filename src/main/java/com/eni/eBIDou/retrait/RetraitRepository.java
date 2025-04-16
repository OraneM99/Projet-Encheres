package com.eni.eBIDou.retrait;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetraitRepository extends JpaRepository<Retrait, Long> {

    Retrait findRetraitByArticle_NoArticle(Long id);

    List<Retrait> findByVille(String ville);

    List<Retrait> findByCodePostal(String codePostal);
}
