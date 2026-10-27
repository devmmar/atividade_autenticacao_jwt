package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.models.TipoUsuario;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.UserRepository;

@Configuration
public class AdminBootstrap {
    @Bean
    ApplicationRunner createInitialAdmin(
            UserRepository users,
            PasswordEncoder passwords,
            @Value("${bootstrap.admin.email:}") String email,
            @Value("${bootstrap.admin.password:}") String password) {
        return args -> {
            if (email.isBlank() || password.isBlank()) return;
            if (users.findByEmailIgnoreCase(email).isPresent()) return;
            UserModel admin = new UserModel();
            admin.setNome("Administrador local");
            admin.setEmail(email.trim().toLowerCase());
            admin.setSenha(passwords.encode(password));
            admin.setTipo(TipoUsuario.ADMIN);
            users.save(admin);
        };
    }
}
