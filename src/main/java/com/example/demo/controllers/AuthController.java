package com.example.demo.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.RegisterRequest;
import com.example.demo.dtos.UserDTO;
import com.example.demo.models.TipoUsuario;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/public")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final JwtService tokens;

    public AuthController(UserRepository users, PasswordEncoder passwords, JwtService tokens) {
        this.users = users;
        this.passwords = passwords;
        this.tokens = tokens;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (users.findByEmailIgnoreCase(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", "E-mail ja cadastrado"));
        }
        UserModel user = new UserModel();
        user.setNome(request.nome().trim());
        user.setEmail(email);
        user.setSenha(passwords.encode(request.password()));
        user.setTipo(TipoUsuario.COMUM);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(users.save(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        UserModel user = users.findByEmailIgnoreCase(request.email().trim())
                .filter(found -> found.getSenha() != null && passwords.matches(request.password(), found.getSenha()))
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", "Credenciais invalidas"));
        }

        String token = tokens.create(user);
        return ResponseEntity.ok(Map.of("token", token, "type", "Bearer", "expiresIn", 3600));
    }
}
