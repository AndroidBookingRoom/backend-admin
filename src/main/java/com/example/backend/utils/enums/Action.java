package com.example.backend.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {
    ADD("ADD"),
    UPDATE("UPDATE");

    private final String value;
}
