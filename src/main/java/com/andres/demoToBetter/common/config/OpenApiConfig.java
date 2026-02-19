package com.andres.demotobetter.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Configuration class for OpenAPI.
 * 
 * @author andres
 */
@Configuration
public class OpenApiConfig {
        @Bean
        public OpenAPI customOpenAPI() {
                final String securitySchemeName = "bearerAuth";
                return new OpenAPI()
                                .info(new Info()
                                                .title("DemoToBetter API")
                                                .version("1.0")
                                                .description("API documentation with traceability and JWT security"))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName,
                                                                new SecurityScheme()
                                                                                .name(securitySchemeName)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}