package com.amaris.orizon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orizonOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orizon API - Module CRA")
                        .description("Système de gestion des imputations")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("PDA Camunda Squad")
                                .url("https://pdacamunda.com")));
    }
}