package com.example.userservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserSyncFilter userSyncFilter) throws Exception {
        http
                // Autoriser CORS
                .cors(Customizer.withDefaults())

                // Désactiver CSRF pour les API REST
                .csrf(csrf -> csrf.disable())

                // Autoriser tout utilisateur authentifié, sans exigence de rôle
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").authenticated()
                        .anyRequest().permitAll()
                )

                // Configurer le serveur de ressources OAuth2 pour décoder le JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        // Ajouter ton filtre custom après l’authentification
        http.addFilterAfter(userSyncFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
