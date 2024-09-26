package com.example.backend.services.impl;

import com.example.backend.entity.UserRole;
import com.example.backend.repositorys.UserRoleRepository;
import com.example.backend.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository  userRoleRepository;

    @Override
    public List<UserRole> getUserRoleByIdUser(Long idUser) {
        return userRoleRepository.findUserRoleByIdUser(idUser);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
