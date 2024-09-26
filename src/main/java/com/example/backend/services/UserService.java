package com.example.backend.services;

import com.example.backend.domain.request.RequestUserDTO;
import com.example.backend.domain.response.ResponseUserDTO;
import com.example.backend.entity.User;
import com.example.backend.utils.enums.StatusRegisterUserEnum;

public interface UserService {
    User findUserByUsername(String username);

    StatusRegisterUserEnum createUser(RequestUserDTO request);
}
