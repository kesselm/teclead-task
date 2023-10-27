package com.example.tecleadtask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
@EnableWebMvc
public class ApplicationConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    public OpenAPI openAPI() {
        var devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        var prodServer = new Server();
        prodServer.setUrl("http://localhost:9090");
        prodServer.setDescription("Server URL in Production environment");

        var contact = new Contact();
        contact.setEmail("zinow@gmx.de");
        contact.setName("kesselm");
        contact.setUrl("https://github.com/kesselm");

        var info = new Info()
                .title("Teclead Task API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to teclead task app.");

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
