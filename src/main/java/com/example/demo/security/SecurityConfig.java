package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) -> response.sendError(401))
                        .accessDeniedHandler((request, response, exception) -> response.sendError(403)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/register", "/public/login", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/imoveis", "/imoveis/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/imoveis/sabermais/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/imoveis/*/status").authenticated()
                        .requestMatchers("/imoveis/**", "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/bairros/**", "/tiposimoveis/**",
                                "/tipos-imoveis/**", "/fotos/**", "/fotos-imoveis/**").permitAll()
                        .requestMatchers("/bairros/**", "/tiposimoveis/**", "/tipos-imoveis/**",
                                "/fotos/**", "/fotos-imoveis/**").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
