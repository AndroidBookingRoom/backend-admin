package com.example.backend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by NhanNguyen on 4/28/2021
 *
 * @author: NhanNguyen
 * @date: 4/28/2021
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);


    @Override
    public void commence(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Fail");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "401");
    }
}