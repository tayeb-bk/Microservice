package com.example.userservice.Controller;

import com.example.userservice.Service.UserService;
import com.example.userservice.UserSynchronizer;
import com.example.userservice.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserSynchronizer userSynchronizer;

    public UserController(UserService userService, UserSynchronizer userSynchronizer) {
        this.userService = userService;
        this.userSynchronizer = userSynchronizer;
    }


    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) throw new RuntimeException("JWT manquant");
        // Synchroniser avant de retourner
        return userSynchronizer.synchronizeUser(jwt);
    }

    @GetMapping("/sync")
    public User syncCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) throw new RuntimeException("JWT manquant");
        User user = userSynchronizer.synchronizeUser(jwt);
        System.out.println("✅ User synchronized via /sync: " + user.getUsername());
        return user;
    }
}

