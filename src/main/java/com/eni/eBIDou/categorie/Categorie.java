package com.eni.eBIDou.categorie;

import com.eni.eBIDou.article.Article;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Categorie {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long noCategorie;
    private String libelle;

    //@OneToMany(mappedBy = "categorie")
    private List<Article> articles;

}
