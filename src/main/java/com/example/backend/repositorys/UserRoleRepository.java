package com.example.backend.repositorys;

import com.example.backend.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    List<UserRole> findUserRoleByIdUser(Long idUser);
}
