package com.example.backend.exceptions;

import com.example.backend.common.Constants;
import com.example.backend.domain.Response;
import com.example.backend.utils.enums.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody Response handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach((err) -> {
            errors.put(((FieldError) err).getField(), err.getDefaultMessage());
        });
        log.error(ex.getMessage(), ex);
        return Response.error(Constants.RESPONSE_TYPE.VALIDATION,
                ErrorCodes.BR0001.getCode(),ErrorCodes.BR0001.getMessage(), errors);
    }

//    @ExceptionHandler({ AuthenticationException.class })
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public @ResponseBody Response handleAuthenticationException(Exception ex)  {
//        return Response.error(Constants.RESPONSE_TYPE.ACCESS_DENIED,
//                ErrorCodes.FORBIDDEN.getCode(), ErrorCodes.FORBIDDEN.getMessage());
//    }

    @ExceptionHandler({ ResponseStatusException.class })
    public @ResponseBody Response handlerNotFoundException(ResponseStatusException ex)  {
        log.error(ex.getMessage(), ex);
        return Response.error(Constants.RESPONSE_TYPE.WARNING,
                String.valueOf(ex.getStatusCode().value()), ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    public @ResponseBody Response handleAnyException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Response.error(Constants.RESPONSE_TYPE.ERROR,
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
    }
}
