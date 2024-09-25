package com.example.backend.services;

import com.example.backend.entity.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getListRoleForUser(Long idUser);
}
