package com.sporty.bookstore.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:56 PM
 */
@Configuration
public class SwaggerConfig {

    private static final String VERSION = "V1";
    private static final String TITLE = "Book Inventory";
    private static final String API_DESCRIPTION = "Book Inventory API";

    @Bean
    public GroupedOpenApi publicOpenAPI() {
        return GroupedOpenApi
                .builder()
                .group("public-apis")
                .packagesToScan("com.sporty.bookstore")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .version(VERSION)
                        .description(API_DESCRIPTION));
    }
}
