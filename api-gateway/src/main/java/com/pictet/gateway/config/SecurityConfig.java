package com.pictet.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/actuator/health")
                        .permitAll()
                        .pathMatchers("/api/books",
                                "/api/books/**")
                        .permitAll()
                        .pathMatchers("/api/adventures",
                                "/api/adventures/**")
                        .permitAll()
                        .anyExchange()
                        .denyAll()
                )
                .build();
    }
}
