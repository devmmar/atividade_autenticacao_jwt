package com.example.demo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.models.UserModel;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(@Value("${security.jwt.secret}") String encoded) {
        byte[] secret;
        try {
            secret = Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("JWT_SECRET deve conter uma chave Base64 valida", exception);
        }
        if (secret.length < 32) {
            throw new IllegalStateException("JWT_SECRET deve conter ao menos 32 bytes aleatorios em Base64");
        }
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).withIssuer("aula05").build();
    }

    public String create(UserModel user) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer("aula05")
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getTipo().name())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }
}
