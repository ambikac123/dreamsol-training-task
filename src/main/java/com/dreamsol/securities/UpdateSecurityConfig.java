package com.dreamsol.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class UpdateSecurityConfig
{
    @Autowired private DynamicUrlAndAuthorityService dynamicUrlAndAuthorityService;
    @Autowired private SecurityConfig securityConfig;
    public void updateSecurityConfig() throws Exception {
        HttpSecurity httpSecurity = securityConfig.getHttpSecurity();
        httpSecurity.authorizeHttpRequests(auth->auth
                .requestMatchers(dynamicUrlAndAuthorityService.getDynamicUrlPatterns())
                .hasAnyAuthority(dynamicUrlAndAuthorityService.getDynamicAuthorities())
        );
    }
}
