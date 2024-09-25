package com.example.backend.config;

import com.example.backend.domain.CustomUserDetails;
import com.example.backend.entity.User;
import com.example.backend.services.RoleService;
import com.example.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {
    private final UserService  userService;
    private final RoleService roleService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        user.setRoleSet(roleService.getListRoleForUser(user.getId()));
        return new CustomUserDetails(user);
    }

}
