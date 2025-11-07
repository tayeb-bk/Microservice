package com.example.userservice.Controller;

import com.example.userservice.Service.UserService;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        return userService.getByEmail(email);
    }
}

