package com.example.backend.services;

import com.example.backend.entity.User;

import java.util.Optional;

public interface UserService {
    User findUserByUsername(String username);
}
