package com.eni.eBIDou.enchere;

import com.eni.eBIDou.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnchereRepository extends JpaRepository<Enchere, Long> {

    List<Enchere> findByArticleCible(Article articleCible);
}
