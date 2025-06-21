package com.anyang.maruni.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("MARUNI API")
                        .description("MARUNI REST API 문서입니다.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("김규일")
                                .email("rlarbdlf222@gmail.com")
                                .url("https://github.com/Kimgyuilli"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
