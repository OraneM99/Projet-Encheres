package com.eni.eBIDou.article;

import com.eni.eBIDou.retrait.Retrait;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFormDTO {

    private Article article;
    private Retrait retrait;

}
