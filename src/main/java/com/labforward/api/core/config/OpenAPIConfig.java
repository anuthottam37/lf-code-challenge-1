package com.labforward.api.core.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Greetings Task Labforward", version = "1.0", description = "create, update, and delete greetings", contact = @Contact(email = "anuthottam37@gmail.com", name = "Anu")))
public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("hello-apis-public")
                .pathsToMatch("/hello/**")
                .build();
    }
}
