package com.conviteplus.conviteplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Convite+ API", 
        version = "1.0", 
        description = "Documentação da API do Convite+"
      
    ),
    servers = {@Server(url = "http://localhost:8080", description = "Servidor local")}
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Convite+ API")
                .version("1.0")
                .description("Documentação da API do Convite+")
                .contact(new Contact().name("Suporte Convite+").email("suporte@conviteplus.com").url("http://www.conviteplus.com"))
            );
    }
}
