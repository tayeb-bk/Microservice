package com.example.userservice;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.Repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserSynchronizer {

    private final UserRepository userRepository;

    public UserSynchronizer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User synchronizeUser(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String keycloakId = jwt.getSubject();

        Role role = extractRole(jwt);

        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            User user = existing.get();
            user.setEmail(email);
            user.setRole(role);
            return userRepository.save(user);
        } else {
            User newUser = new User();
            newUser.setId(keycloakId);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setRole(role);
            return userRepository.save(newUser);
        }
    }

    private Role extractRole(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            var roles = (Iterable<?>) realmAccess.get("roles");
            for (Object r : roles) {
                if ("admin".equalsIgnoreCase(r.toString())) {
                    return Role.ADMIN;
                }
            }
        }
        return Role.USER;
    }
}
