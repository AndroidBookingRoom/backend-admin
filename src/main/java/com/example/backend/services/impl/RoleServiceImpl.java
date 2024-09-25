package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.entity.Role;
import com.example.backend.entity.UserRole;
import com.example.backend.repositorys.RoleRepository;
import com.example.backend.services.RoleService;
import com.example.backend.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final UserRoleService userRoleService;
    private final RoleRepository roleRepository;

    @Override
    public Set<Role> getListRoleForUser(Long idUser) {
        Set<Role> roles = new HashSet<>();
        List<UserRole> userRoleList = userRoleService.getUserRoleByIdUser(idUser);
        if (CommonUtils.isEmpty(userRoleList)) {
            log.error("Not found role for userId: {}", idUser);
            return null;
        }
        for (UserRole userRole : userRoleList) {
            Optional<Role> role = roleRepository.findById(userRole.getIdRole());
            role.ifPresent(roles::add);
        }
        return roles;
    }
}
