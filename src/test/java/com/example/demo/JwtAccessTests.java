package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.models.ImovelModel;
import com.example.demo.models.TipoUsuario;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.ImovelRepository;
import com.example.demo.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = "security.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=")
@AutoConfigureMockMvc
class JwtAccessTests {
    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired ImovelRepository imoveis;
    @Autowired PasswordEncoder passwords;
    @Autowired ObjectMapper json;

    private Integer userId;
    private Integer adminId;
    private Integer imovelId;

    @AfterEach
    void cleanup() {
        if (imovelId != null) imoveis.deleteById(imovelId);
        if (userId != null) users.deleteById(userId);
        if (adminId != null) users.deleteById(adminId);
    }

    @Test
    void routePermissionsAndJwt() throws Exception {
        String email = "jwt-test-" + UUID.randomUUID() + "@example.invalid";
        String registration = json.writeValueAsString(java.util.Map.of(
                "nome", "Teste", "email", email, "password", "senhaforte123", "role", "ADMIN"));
        mvc.perform(post("/public/register").contentType(MediaType.APPLICATION_JSON).content(registration))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.tipo").value("COMUM"))
                .andExpect(jsonPath("$.senha").doesNotExist());
        UserModel common = users.findByEmailIgnoreCase(email).orElseThrow();
        userId = common.getId();
        org.junit.jupiter.api.Assertions.assertNotEquals("senhaforte123", common.getSenha());
        String commonToken = login(email, "senhaforte123");

        UserModel admin = new UserModel();
        admin.setNome("Admin de teste");
        admin.setEmail("jwt-admin-" + UUID.randomUUID() + "@example.invalid");
        admin.setSenha(passwords.encode("senhaforte123"));
        admin.setTipo(TipoUsuario.ADMIN);
        adminId = users.save(admin).getId();
        String adminToken = login(admin.getEmail(), "senhaforte123");

        ImovelModel imovel = new ImovelModel();
        imovel.setTitulo("Teste JWT");
        imovel.setStatus("Disponivel");
        imovelId = imoveis.save(imovel).getId();

        mvc.perform(get("/imoveis")).andExpect(status().isOk());
        mvc.perform(post("/imoveis").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/imoveis").header("Authorization", "Bearer invalido")
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/imoveis").header("Authorization", "Bearer " + commonToken)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
        mvc.perform(patch("/imoveis/{id}/status", imovelId)
                .header("Authorization", "Bearer " + commonToken)
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"Vendido\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("Vendido"));
        mvc.perform(get("/users").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    private String login(String email, String password) throws Exception {
        String response = mvc.perform(post("/public/login").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(java.util.Map.of("email", email, "password", password))))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return json.readTree(response).get("token").asText();
    }
}
