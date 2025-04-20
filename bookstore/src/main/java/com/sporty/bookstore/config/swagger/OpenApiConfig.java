package com.sporty.bookstore.config.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:56 PM
 */
@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "Bearer Authentication",
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    private static final String VERSION = "V1";
    private static final String TITLE = "Book Store";
    private static final String API_DESCRIPTION = "Book Store API";

    @Bean
    public GroupedOpenApi publicOpenAPI() {
        return GroupedOpenApi
                .builder()
                .group("public-apis")
                .pathsToExclude("api/**")
                .packagesToScan("com.sporty.bookstore.controller")
                .build();
    }

    @Bean
    public GroupedOpenApi adminOpenAPI() {
        return GroupedOpenApi
                .builder()
                .pathsToExclude("api/admin/**")
                .group("admin-public-apis")
                .packagesToScan("com.sporty.bookstore.controller.admin")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .version(VERSION)
                        .description(API_DESCRIPTION))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"));
    }
}
