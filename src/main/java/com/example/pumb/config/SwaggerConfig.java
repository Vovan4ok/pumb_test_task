package com.example.pumb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .servers(
                        Arrays.asList(
                                new Server().url("http://localhost:8080")
                        )
                )
                .info(
                        new Info().title("Animal API").version("1.0")
                );
    }
}
