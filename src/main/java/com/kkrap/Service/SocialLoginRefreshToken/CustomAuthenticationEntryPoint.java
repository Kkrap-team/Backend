package com.kkrap.Service.SocialLoginRefreshToken;

import com.kkrap.ResponseDTO.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

//        ErrorResponse errorResponse = ErrorResponse.from(401, authException.getMessage());
        ErrorResponse errorResponse = ErrorResponse.from(401, "유효하지 않은 access token입니다.");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
