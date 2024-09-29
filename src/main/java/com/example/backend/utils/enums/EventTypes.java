package com.example.backend.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTypes {
    COMBO("COMBO"),
    TOUR("TOUR");

    private final String value;
}
