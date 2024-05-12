package com.project.tms.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "TMS",
                description = "TMS api 명세",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    private final String REST_API_ROOT = "/api/**";
    private final String REST_API_GROUP = "TMS REST API";


    @Bean
    public GroupedOpenApi restApi() {

        return GroupedOpenApi.builder()
                .pathsToMatch(REST_API_ROOT)
                .group(REST_API_GROUP)
                .build();
    }


}