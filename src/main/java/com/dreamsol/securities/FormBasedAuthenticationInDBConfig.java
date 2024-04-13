package com.dreamsol.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class FormBasedAuthenticationInDBConfig
{
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/swagger-ui/**").fullyAuthenticated()
                        .requestMatchers("/api/users/download-excel-sample",
                                "/api/users/download-excel-dummy",
                                "/api/departments/download-excel-sample",
                                "/api/departments/download-excel-dummy",
                                "/api/usertypes/download-excel-sample",
                                "/api/usertypes/download-excel-dummy").hasRole("GUEST")
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest()
                        .fullyAuthenticated()
                ).formLogin(Customizer.withDefaults()).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
