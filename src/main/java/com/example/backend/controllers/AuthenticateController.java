package com.example.backend.controllers;

import com.example.backend.common.Constants;
import com.example.backend.config.JwtUtil;
import com.example.backend.domain.Response;
import com.example.backend.domain.request.RequestUserDTO;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
public class AuthenticateController {
    private final AuthenticationManager authenticationManager;

    @Qualifier("userDetailsService")
    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Response createAuthenticationTokenAndRole(@Valid @RequestBody RequestUserDTO request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            return Response.warning(Constants.RESPONSE_CODE.WARNING,e.getMessage());
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final List<String> roles = new ArrayList<>();
        userDetails.getAuthorities().forEach(grantedAuthority -> {
            roles.add(grantedAuthority.getAuthority());
        });
        Map<String, Object> data = new HashMap<>();
        data.put("jwt", jwt);
        data.put("roles", roles);
        return Response.success(Constants.RESPONSE_CODE.SUCCESS).withData(data);
    }
}
