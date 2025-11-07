package com.example.userservice;


import com.example.userservice.Repository.UserRepository;
import com.example.userservice.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserSyncFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public UserSyncFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            String keycloakId = jwt.getSubject();
            String username = jwt.getClaimAsString("preferred_username");
            String email = jwt.getClaimAsString("email");
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");

            // Synchronisation
            userRepository.findById(keycloakId).or(() -> {
                User newUser = new User();
                newUser.setId(keycloakId);
                newUser.setUsername(username);
                newUser.setEmail(email);
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                return java.util.Optional.of(userRepository.save(newUser));
            });
        }

        filterChain.doFilter(request, response);
    }
}
