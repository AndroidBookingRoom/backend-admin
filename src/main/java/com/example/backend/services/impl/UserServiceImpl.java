package com.example.backend.services.impl;

import com.example.backend.domain.request.RequestUserDTO;
import com.example.backend.domain.response.ResponseUserDTO;
import com.example.backend.entity.User;
import com.example.backend.entity.UserRole;
import com.example.backend.repositorys.UserRepository;
import com.example.backend.services.UserRoleService;
import com.example.backend.services.UserService;
import com.example.backend.utils.enums.StatusRegisterUserEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("Not found User with username: %s", username)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusRegisterUserEnum createUser(RequestUserDTO request) {
        log.info("Start Register");
        User user =  User.builder()
                .username(request.getUsername())
                .build();
        try {
//            check existed username
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return StatusRegisterUserEnum.EXISTED_USERNAME;
            }
//            check existed email
//            if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
//                return StatusRegisterUserEnum.Existed_Email;
//            }

            user.setPassword(passwordEncoder.encode(request.getPassword()));
//            UUID uuid = UUID.randomUUID();
//            String guid = uuid.toString();
//            user.setGuid(guid);
//            user.setAccountNonExpired(true);
//            user.setAccountNonLocked(true);
//            user.setCredentialsNonExpired(true);

            userRepository.save(user);

//            insert role user
            UserRole userRole = UserRole.builder()
                    .idRole(1L)
                    .idUser(user.getId())
                    .build();
            userRoleService.save(userRole);
            return StatusRegisterUserEnum.SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage());
            return StatusRegisterUserEnum.SUCCESS;
        }
    }
}
