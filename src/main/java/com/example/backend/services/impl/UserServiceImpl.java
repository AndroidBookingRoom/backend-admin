package com.example.backend.services.impl;

import com.example.backend.entity.User;
import com.example.backend.repositorys.UserRepository;
import com.example.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User findUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("Not found User with username: %s", username)));
    }
}
