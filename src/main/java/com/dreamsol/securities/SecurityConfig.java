package com.dreamsol.securities;

import com.dreamsol.helpers.RoleAndPermissionHelper;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.repositories.RoleRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{
    @Autowired private JwtAuthenticationEntryPoint point;
    @Autowired private JwtAuthenticationFilter filter;
    @Autowired RoleRepository roleRepository;
    @Autowired PermissionRepository permissionRepository;
    @Autowired RoleAndPermissionHelper roleAndPermissionHelper;
    private HttpSecurity httpSecurity;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        this.httpSecurity = http;
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/swagger-ui/**",
                                "/api/login",
                                "/api/logout").permitAll()
                );
        httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(point))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
        /*http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/swagger-ui/**",
                                "/api/login",
                                "/api/logout").permitAll()
                        .requestMatchers(HttpMethod.GET).hasAnyAuthority("READ_ONLY","ADMIN","USER")
                        .requestMatchers(HttpMethod.POST).hasAnyAuthority("WRITE_ONLY","ADMIN")
                        .requestMatchers(HttpMethod.PUT).hasAnyAuthority("READ_WRITE","ADMIN")
                        .requestMatchers(HttpMethod.DELETE).hasAnyAuthority("DELETE","ADMIN")
                        .anyRequest().authenticated()
                ).exceptionHandling(ex->ex.authenticationEntryPoint(point))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class);
        return http.build();*/
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    public void updateSecurityConfig()
    {
        try {
            Map<String, String[]> userAuthorities = roleAndPermissionHelper.getAuthorityPatterns();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Collection<? extends GrantedAuthority> authoritiesList = authentication.getAuthorities();
                List<String> authorityTypes = new ArrayList<>();
                List<String> patternsList = new ArrayList<>();
                for (GrantedAuthority authority : authoritiesList) {
                    String authorityName = authority.getAuthority();
                    authorityTypes.add(authorityName);
                    for(String pattern : userAuthorities.get(authorityName))
                    {
                        patternsList.add(pattern);
                    }
                }
                String[] array1 = patternsList.toArray(new String[]{});
                String[] array2 = authorityTypes.toArray(new String[]{});
                httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers(array1).hasAnyAuthority(array2)
                );
            }
            httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        }catch (Exception e)
        {
            throw new RuntimeException("Error occurred while granting permissions, Reason: "+e.getMessage());
        }
    }
}
