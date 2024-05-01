package com.dreamsol.securities;

import com.dreamsol.exceptions.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    @Autowired private JwtHelper jwtHelper;
    @Autowired private UserDetailsService userDetailsService;
    private final String[] PUBLIC_URLS = {
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/favicon-32x32.png",
            "/v3/api-docs",
            "/api/login",
            "/api/re-generate-token",
            "/api/update-endpoints",
            "/api/get-endpoints"
    };
    List<String> publicUrls = List.of(PUBLIC_URLS);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        if(!publicUrls.contains(request.getRequestURI()))
        {
            String requestToken = request.getHeader("Authorization");
            String username = null;
            String actualToken = null;
            if(requestToken!=null && requestToken.startsWith("Bearer"))
            {
                actualToken = requestToken.substring(7);
                try{
                    username = jwtHelper.getUsernameFromToken(actualToken);
                }catch(ExpiredJwtException e)
                {
                    throw new RuntimeException(e.getMessage());
                }
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if(jwtHelper.validateToken(actualToken,userDetails))
                    {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                    else{
                        response.sendError(403,"Invalid token!");
                    }
                }
            }else{
                response.sendError(403,"Token is in incorrect format. missing prefix 'Bearer'");
            }
        }
        filterChain.doFilter(request,response);
    }
}


/*
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String AUTH_HEADER = "Authorization";
    private static final String LOGIN_URI = "/api/login";

    @Autowired private JwtHelper jwtHelper;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private LoginUserRepository loginUserRepository;
    @Autowired private AuthenticationEntryPointImpl entryPoint;
    private final String[] PUBLIC_URLS = {
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/favicon-32x32.png",
            "/v3/api-docs",
            "/api/login",
            "/api/re-generate-token",
            "/api/update-endpoints",
            "/api/get-endpoints"
    };
    List<String> publicUrls = List.of(PUBLIC_URLS);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();
        if(publicUrls.indexOf(requestURI)!=-1)
        {
            AnonymousAuthenticationToken authenticationToken = new AnonymousAuthenticationToken(requestURI,requestURI,List.of(new SimpleGrantedAuthority("AnonymousUser")));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
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
                    UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
                    if(jwtHelper.validateToken(actualToken,userDetails))
                    {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                     }
                }

            } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                sendError(response, e.getMessage(), HttpStatus.FORBIDDEN);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private void sendError(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.sendError(status.value(),message);
    }
}*/
