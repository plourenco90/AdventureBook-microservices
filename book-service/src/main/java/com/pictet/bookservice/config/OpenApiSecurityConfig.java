package com.pictet.bookservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiSecurityConfig {

    @Value("${app.security.keycloak.token-url}")
    private String tokenUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "keycloak_client_credentials";

        return new OpenAPI().info(new Info().title("Book Service API").version("0.0.1"))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows().clientCredentials(new OAuthFlow().tokenUrl(tokenUrl)))));
    }
}
