package com.dreamsol.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


public class FormBasedAuthenticationInDBConfig
{
    @Bean
    public SecurityFilterChain filterSecurityChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/users/download-excel-sample",
                                "/api/users/download-excel-dummy",
                                "/api/departments/download-excel-sample",
                                "/api/departments/download-excel-dummy",
                                "/api/usertypes/download-excel-sample",
                                "/api/usertypes/download-excel-dummy").hasRole("GUEST")
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/departments/**",
                                "/api/usertypes/**",
                                "/api/users/**",
                                "/api/roles/**").hasRole("ADMIN")
                        .anyRequest()
                        .fullyAuthenticated()
                ).formLogin(Customizer.withDefaults()).build();
    }
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
