package com.example.userservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final UserSyncFilter userSyncFilter;

    public SecurityConfig(UserSyncFilter userSyncFilter) {
        this.userSyncFilter = userSyncFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // désactive CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").authenticated()
                        .anyRequest().permitAll()
                )
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.addAllowedOriginPattern("*");
                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    return config;
                }))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})  // nouvelle syntaxe 6.1+
                );

        // Ajouter le filtre après BearerTokenAuthenticationFilter
        http.addFilterAfter(userSyncFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
