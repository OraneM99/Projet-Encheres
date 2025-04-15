package com.eni.eBIDou.article;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleServiceIMPL implements ArticleService {

    public List<ArticleBO> articlesLists = new ArrayList<>();

    public ArticleServiceIMPL() {

        articlesLists.add(new ArticleBO(1L, "Trombone", "Ca permet de pas regrouper des feuilles", LocalDateTime.of(2025,04,01,10,05) , 5.2f));
        articlesLists.add(new ArticleBO(2L, "bout de scotch", "Peut encore servir", LocalDateTime.of(2025,04,01,10,05), 2.5f));
        articlesLists.add(new ArticleBO(3L, "Cuillère cassée", "Faudra la laver", LocalDateTime.of(2025,04,01,10,05), 1.5f));
        articlesLists.add(new ArticleBO(4L, "éponge salle", "On ne sait jamais", LocalDateTime.of(2025,04,01,10,05) , 1.5f));

    }

    @Override
    public List<ArticleBO> selectAll() {
        return articlesLists;
    }

    @Override
    public ArticleBO selectById(long id) {
        return null;
    }

    @Override
    public ArticleBO selectByName(String name) {
        return null;
    }
}
