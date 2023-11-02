package com.dev.redditclone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo(){
        return new OpenAPI()
                .info(new Info()
                        .title("Spring reddit api")
                        .description("spring boot rest api consumed by angular client")
                        .license(new License().name("Apache License, Version 2.0"))
                        .contact(new Contact().name("Ahmed Rafat").email("ahmedrafatfci@gmail.com"))
                        .version("1.0.0")
                );
    }

}
