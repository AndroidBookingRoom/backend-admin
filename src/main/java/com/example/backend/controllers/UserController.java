package com.example.backend.controllers;

import com.example.backend.domain.Response;
import com.example.backend.domain.request.RequestUserDTO;
import com.example.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public @ResponseBody Response createUser(@Valid @RequestBody RequestUserDTO request) {
        userService.createUser(request);
        return Response.success();
    }
}
