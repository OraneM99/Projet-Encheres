package com.eni.eBIDou.Security;

import com.eni.eBIDou.utilisateurs.UtilisateurBO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordToken {

    @Id
    private String token;

    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private UtilisateurBO utilisateur;

    private LocalDateTime expiration;
}
