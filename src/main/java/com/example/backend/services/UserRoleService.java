package com.example.backend.services;

import com.example.backend.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> getUserRoleByIdUser(Long idUser);
}
