package com.example.backend.exceptions;

import com.example.backend.common.Constants;
import com.example.backend.domain.Response;
import com.example.backend.utils.enums.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlingController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody Response handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach((err) -> {
            errors.put(((FieldError) err).getField(), err.getDefaultMessage());
        });
        return Response.error(Constants.RESPONSE_TYPE.VALIDATION,
                ErrorCodes.BR0001.getCode(),ErrorCodes.BR0001.getMessage(), errors);
    }

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody Response handleAuthenticationException(Exception ex)  {
        return Response.error(Constants.RESPONSE_TYPE.ACCESS_DENIED,
                ErrorCodes.FORBIDDEN.getCode(), ErrorCodes.FORBIDDEN.getMessage());
    }

}
