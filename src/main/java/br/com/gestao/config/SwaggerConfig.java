package br.com.gestao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI(
            @Value("${swagger.app.version}") String appVersion,
            @Value("${swagger.app.title}") String appTitle,
            @Value("${swagger.app.description}") String appDescription) {
        return new OpenAPI().info(new Info()
                .title(appTitle)
                .version(appVersion)
                .description(appDescription)
                .license(new License().name("Gestão de Pessoas")));
    }
    
}