package com.dreamsol.securities;

import com.dreamsol.entities.LoginUser;
import com.dreamsol.repositories.LoginUserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String AUTH_HEADER = "Authorization";
    private static final String LOGIN_URI = "/api/login";
    private static final String RE_GENERATE_TOKEN_URL = "/api/re-generate-token";

    @Autowired private JwtHelper jwtHelper;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private LoginUserRepository loginUserRepository;
    @Autowired private JwtAuthenticationEntryPoint entryPoint;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if(requestURI.equals(LOGIN_URI))
        {
            if(!requestForLogin(request, response))
            {
                sendError(response,"Login Failed: The requested client machine is already in use i.e. A user already logged in with this machine.",HttpStatus.FORBIDDEN);
                return;
            }
        }else if(requestURI.equals(RE_GENERATE_TOKEN_URL)){

            AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken("anonymous", request.getRemoteAddr(), Arrays.asList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else {
            String tokenHeader = request.getHeader(AUTH_HEADER);
            if (tokenHeader == null || !tokenHeader.startsWith(TOKEN_PREFIX)) {
                sendError(response, "Token must be provided in correct format, It must be a 'Bearer' token", HttpStatus.FORBIDDEN);
                return;
            }

            String actualToken = tokenHeader.substring(TOKEN_PREFIX.length()).trim();
            try {
                String usernameFromToken = jwtHelper.getUsernameFromToken(actualToken);
                if (usernameFromToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    LoginUser loginUser = loginUserRepository.findByUsername(usernameFromToken);
                    if(loginUser==null)
                    {
                        sendError(response,"You are logged out. Please! login again to access this resource!",HttpStatus.BAD_REQUEST);
                        return;
                    }
                    String tokenIP = jwtHelper.getClaimFromToken(actualToken, (claims -> claims.get("IP"))).toString();
                    if (tokenIP.equals(request.getRemoteAddr())) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        sendError(response, "Requested token and requested user not matched!", HttpStatus.FORBIDDEN);
                        return;
                    }
                }
            } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                sendError(response, e.getMessage(), HttpStatus.FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean requestForLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientIP = request.getRemoteAddr();
        LoginUser loginUser = loginUserRepository.findByIpAddress(clientIP);
        if (loginUser != null) {
            return false;
        } else {
            AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken("anonymous", request.getRemoteAddr(), Arrays.asList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }
    }

    private void sendError(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        PrintWriter printWriter = response.getWriter();
        response.setStatus(status.value());
        printWriter.println(message);
        printWriter.flush();
    }
}
