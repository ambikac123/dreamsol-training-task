package com.dreamsol.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class FormBasedAuthenticationInMemoryConfig
{
    // In memory authentication (user credentials will be stored in memory)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder)
    {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();
        UserDetails guest = User.withUsername("guest")
                .password(passwordEncoder.encode("guest123"))
                .roles("GUEST")
                .build();
        return new InMemoryUserDetailsManager(admin,user,guest);
    }
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

       /*return http.csrf(csrf->csrf.disable())
               .authorizeHttpRequests(auth->auth
                       .requestMatchers("/api/departments/**")
                       .hasRole("ADMIN")
                       .requestMatchers("/api/users/get-all")
                       .hasRole("USER")
                       .anyRequest()
                       .fullyAuthenticated()
               ).formLogin(Customizer.withDefaults()).build();*/
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
