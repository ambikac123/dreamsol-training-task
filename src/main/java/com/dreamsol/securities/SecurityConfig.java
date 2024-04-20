package com.dreamsol.securities;

import com.dreamsol.helpers.EndpointMappingsHelper;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    @Autowired EndpointMappingsHelper endpointMappingsHelper;
    private HttpSecurity httpSecurity;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        this.httpSecurity = http;
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**",
                                "/api/login",
                                "/api/logout",
                                "/api/re-generate-token").permitAll()
                );
        httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(point))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    public void updateSecurityConfig()
    {
        try
        {
            Map<String, String[]> allRoleAndPermissionMap = roleAndPermissionHelper.getAllRoleAndPermissionMap();
            Map<String,String> endpointMappings = endpointMappingsHelper.getEndpointMappings();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null)
            {
                Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
                Set<String> authorityTypes = new HashSet<>();
                Set<String> patternsList = new HashSet<>();
                for(GrantedAuthority authority : userAuthorities)
                {
                    String authorityName = authority.getAuthority();
                    authorityTypes.add(authorityName);
                    patternsList.addAll(
                            Arrays.stream(allRoleAndPermissionMap.get(authorityName))
                                    .map(endpointMappings::get)
                                    .toList()
                    );
                }
                httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers(patternsList.toArray(new String[]{}))
                        .hasAnyAuthority(authorityTypes.toArray(new String[]{}))
                );
            }
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
