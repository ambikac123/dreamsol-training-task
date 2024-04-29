package com.dreamsol.securities;

import com.dreamsol.helpers.RoleAndPermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{
    @Autowired private JwtAuthenticationEntryPoint entryPoint;
    @Autowired private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired private JwtAuthenticationFilter filter;
    @Autowired private RoleAndPermissionHelper roleAndPermissionHelper;
    private HttpSecurity httpSecurity;
    private final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/login",
            "/api/re-generate-token",
            "/api/update-endpoints",
            "/api/get-endpoints"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        System.out.println("SecurityConfig->securityFilterChain()");
        httpSecurity = http;
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                );
        httpSecurity.exceptionHandling(exception->exception
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    public void updateSecurityConfig()
    {
        try
        {
            Map<String,String[]> authorityNamesAndUrlsMap = roleAndPermissionHelper.getAuthorityNameAndUrlsMap();
            Set<String> authorityTypes = new HashSet<>();
            Set<String> authorityUrls = new HashSet<>();
            for(Map.Entry<String,String[]> authorityNameAndUrl : authorityNamesAndUrlsMap.entrySet())
            {
                authorityTypes.add(authorityNameAndUrl.getKey());
                authorityUrls.addAll(Arrays.asList(authorityNameAndUrl.getValue()));
            }
            authorityUrls.add("/api/logout");
            httpSecurity.authorizeHttpRequests(auth->auth
                    .requestMatchers(authorityUrls.toArray(new String[]{}))
                    .hasAnyAuthority(authorityTypes.toArray(new String[]{}))
            );
            System.out.println("SecurityConfig->updateSecurityConfig()->httpSecurity: "+httpSecurity);
        }catch (Exception e)
        {
            throw new RuntimeException("Error occurred while applying permissions, Reason: "+e.getMessage());
        }
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
