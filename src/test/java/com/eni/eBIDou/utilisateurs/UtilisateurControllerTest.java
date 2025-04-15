package com.eni.eBIDou.utilisateurs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindAll() throws Exception {
        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUtilisateur() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setPseudo("rudy");
        dto.setNom("Guillaume");
        dto.setPrenom("Rudy");
        dto.setEmail("rudy@test.com");
        dto.setTelephone("0600000000");
        dto.setRue("1 rue ici");
        dto.setCodePostal("44000");
        dto.setVille("Nantes");
        dto.setCredit(0);
        dto.setAdministrateur(false);
        dto.setMotDePasse("monMotDePasse");

        mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pseudo").value("rudy"));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/utilisateurs/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUtilisateurNotFound() throws Exception {
        mockMvc.perform(delete("/api/utilisateurs/9999"))
                .andExpect(status().isNotFound());
    }
}
