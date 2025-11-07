package com.example.userservice.Service;

import com.example.userservice.Repository.UserRepository;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}

