package com.example.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTypeRoomDTO {
    private Long id;
    @NotBlank
    private String name;
    private Boolean useYN;
}