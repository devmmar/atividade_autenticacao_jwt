package com.example.demo.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService tokens;
    private final UserRepository users;

    public JwtFilter(JwtService tokens, UserRepository users) {
        this.tokens = tokens;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null) {
            if (!header.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            try {
                DecodedJWT jwt = tokens.verify(header.substring(7));
                Integer id = Integer.valueOf(jwt.getSubject());
                UserModel user = users.findById(id).orElseThrow();
                if (!user.getEmail().equalsIgnoreCase(jwt.getClaim("email").asString())) {
                    throw new JWTVerificationException("Usuario do token nao corresponde ao cadastro");
                }
                var authentication = new UsernamePasswordAuthenticationToken(
                        user.getId(), null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getTipo().name())));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException | IllegalArgumentException | java.util.NoSuchElementException exception) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
