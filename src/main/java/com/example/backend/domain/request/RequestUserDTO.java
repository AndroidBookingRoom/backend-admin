package com.example.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestUserDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
