package com.example.backend.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
    BR0001("BR0001", "Validate data"),
    SUCCESS("200", "Successfully!");
    private final String code;
    private final String message;

}
