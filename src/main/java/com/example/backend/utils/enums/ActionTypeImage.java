package com.example.backend.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionTypeImage {
    HOTEL("HOTEL"),
    ROOM("ROOM");

    private final String value;
}
