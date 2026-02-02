package com.dospace.dosurvey.config;

import com.dospace.dosurvey.web.TenantContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

/**
 * Security configuration for DoSurvey service.
 * <p>
 * This service runs behind the API Gateway which handles authentication.
 * We permit all requests and only use Spring Security to properly integrate
 * the TenantContextFilter into the filter chain for exception handling.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TenantContextFilter tenantContextFilter)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterAfter(tenantContextFilter, SecurityContextHolderFilter.class)
                .build();
    }
}
