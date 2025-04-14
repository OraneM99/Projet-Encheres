package com.eni.eBIDou.retrait;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetraitBO {
    private String rue;
    private String codePostal;
    private String ville;
}
