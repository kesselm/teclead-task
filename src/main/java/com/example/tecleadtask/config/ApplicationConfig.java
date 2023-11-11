package com.example.tecleadtask.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebMvc
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApplicationConfig {

    String serverAddress = "http://localhost";

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
    public OpenAPI openAPI() throws JSONException {

        JSONObject findUser = new JSONObject();
        findUser.put("href", "http://localhost:8080/api/v1/users/827");

        JSONObject user = new JSONObject();
        user.put("id", 827);
        user.put("name", "Keßel");
        user.put("vorname", "Martin");
        user.put("email", "martin@kessel.de");
        user.put("_links", findUser);

        JSONArray users = new JSONArray();
        users.put(user);

        JSONObject embedded = new JSONObject();
        embedded.put("Users", users);

        JSONObject before = new JSONObject();
        before.put("href", "http://localhost:8080/api/v1/users?page149&size=1&field=vorname&sortAlg=ASC");

        JSONObject next = new JSONObject();
        next.put("href", "http://localhost:8080/api/v1/users?page151&size=1&field=vorname&sortAlg=ASC");

        JSONObject links = new JSONObject();
        links.put("before", before);
        links.put("next", next);

        var response = new Schema<Map<String, Object>>()
                .addProperty("_embedded", new JsonSchema().example(embedded.toString()))
                .addProperty("_links", new JsonSchema().example(links.toString()));

        var devServer = new Server();
        devServer.setUrl(serverAddress + ":8080");
        devServer.setDescription("Server URL in Development environment");

        var prodServer = new Server();
        prodServer.setUrl(serverAddress + ":9090");
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

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer)).components(new Components()
                .addSchemas("Collection", response)
        );
    }
}
