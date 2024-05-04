package com.dreamsol.securities;

import com.dreamsol.helpers.RoleAndPermissionHelper;
import com.dreamsol.services.impl.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Autowired private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired private RoleAndPermissionHelper roleAndPermissionHelper;
    private final String RESOURCE_ROOT_URL = "/api/**";
    private final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/v3/api-docs/swagger-config",
            "/v3/api-docs",
            "/api/login",
            "/api/re-generate-token",
            "/api/update-endpoints",
            "/api/get-endpoints",
            "/api/get-role-endpoints",
            "/api/get-permission-endpoints"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(auth->auth
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(RESOURCE_ROOT_URL).access(customAuthorizationManager())
        );
        httpSecurity.sessionManagement(session->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity.exceptionHandling(exception->exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception
    {
        return builder.getAuthenticationManager();
    }

    public AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager() {
        return (supplier, object) -> {
            Authentication authentication = supplier.get();
            if(authentication.getPrincipal().equals("anonymousUser"))
                throw new AuthenticationException("User not authenticated!") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                };
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
            String[] patterns = roleAndPermissionHelper.getUserRelatedUrls(userDetailsImpl.getUser());
            AntPathMatcher matcher = new AntPathMatcher("/");
            boolean flag = false;
            HttpServletRequest httpServletRequest = object.getRequest();
            String requestUrlPattern = httpServletRequest.getRequestURI();
            for(String pattern : patterns)
            {
                if(matcher.match(pattern,requestUrlPattern))
                {
                    flag = true;
                    break;
                }
            }
            return new AuthorizationDecision(flag);
        };
    }
}