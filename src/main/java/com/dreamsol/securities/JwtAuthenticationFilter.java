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
            "/api/get-endpoints",
            "/api/get-role-endpoints",
            "/api/get-permission-endpoints"
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
