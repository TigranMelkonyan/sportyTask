package com.sporty.bookstore.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 12:30 PM
 */
@Component
public class ApplicationAuthEntryPoint implements AuthenticationEntryPoint {

    public void commence(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.sendError(401, "Unauthorized");
    }
}
