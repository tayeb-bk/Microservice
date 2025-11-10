package com.example.userservice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserSyncFilter extends OncePerRequestFilter {

    private final UserSynchronizer userSynchronizer;

    public UserSyncFilter(UserSynchronizer userSynchronizer) {
        this.userSynchronizer = userSynchronizer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            try {
                var user = userSynchronizer.synchronizeUser(jwt);
                System.out.println("✅ User synchronized: " + user.getUsername() + " (" + user.getRole() + ")");
            } catch (Exception e) {
                System.err.println("❌ Error during user synchronization: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ Aucun JWT détecté pour cette requête");
        }

        filterChain.doFilter(request, response);
    }
}
