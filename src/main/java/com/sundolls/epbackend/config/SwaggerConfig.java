package com.sundolls.epbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String API_NAME = "Eagle_pop";
    private static final String API_VERSION = "1.0";
    private static final String API_DESCRIPTION = "Eagle_pop 서버 API 문서";

    //API 정보, 보안 컨텍스트 및 보안 체계를 포함하도록 API Docket 빈을 구성
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)                // API 이름지정
                .version(API_VERSION)           // API 버전
                .description(API_DESCRIPTION)   // API 설명
                .build();
    }
}
