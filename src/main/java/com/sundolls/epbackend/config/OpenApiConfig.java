package com.sundolls.epbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    private static final String API_NAME = "Eagle_pop";
    private static final String API_VERSION = "1.0";
    private static final String API_DESCRIPTION = "Eagle_pop 서버 API 문서";
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version(API_VERSION)
                .title(API_NAME)
                .description(API_DESCRIPTION);
        return new OpenAPI().info(info);
    }
}
