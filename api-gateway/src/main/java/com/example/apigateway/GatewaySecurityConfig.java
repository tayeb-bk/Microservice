package com.example.apigateway;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // Désactive CSRF (inutile pour API Gateway REST)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Règles d’autorisation
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/users/**", "/api/posts/**").authenticated()
                        .anyExchange().permitAll()
                )

                // Nouvelle syntaxe pour OAuth2 Resource Server (JWT)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {})
                );

        return http.build();
    }
}

