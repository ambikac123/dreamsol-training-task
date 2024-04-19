package com.dreamsol.securities;

import com.dreamsol.exceptions.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint
{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException
    {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        StringBuilder message = new StringBuilder();
        if(authException.getClass().getSimpleName().equals("ExpiredJwtException"))
        {
            message.append("JWT token has been expired, please re-login");
        }
        else {
            message.append(authException.getMessage());
        }
        writer.println("Access Denied !! "+message);
    }
}
