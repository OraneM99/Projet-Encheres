package com.eni.eBIDou.retrait;

import com.eni.eBIDou.article.Article;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Retrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String rue;
    private String codePostal;
    private String ville;

    @OneToOne
    @JoinColumn(name = "article_noArticle")
    private Article article;


}
