package com.example.tecleadtask.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer))
                .components(new Components()
                .addSchemas("Users", createSchemaForUsers())
                .addSchemas("UserResponse", new JsonSchema().example(createUser().toString()))
                .addSchemas("UserRequest", new JsonSchema().example(createUpdateUserRequestBody().toString()))
        );
    }

    private JSONArray createUsers() {
        JSONArray users = new JSONArray();
        users.put(createUser());
        return users;
    }

    private JSONObject createUser() {
        JSONObject findUser = new JSONObject();
        findUser.put("href", "http://localhost:8080/api/v1/users/827");
        findUser.put("type", "GET");

        JSONObject deleteUser = new JSONObject();
        deleteUser.put("href","http://localhost:8080/api/v1/user/713");
        deleteUser.put("type", "DELETE");

        JSONObject updateUser = new JSONObject();
        updateUser.put("href","http://localhost:8080/api/v1/user");
        updateUser.put("type", "PUT");

        JSONObject findUserByVorname = new JSONObject();
        findUserByVorname.put("href", "http://localhost:8080/api/v1/user?vorname=Martin");
        findUserByVorname.put("type", "GET");

        JSONObject userLinks = new JSONObject();
        userLinks.put("findUser", findUser);
        userLinks.put("deleteUser", deleteUser);
        userLinks.put("updateUser", updateUser);
        userLinks.put("findUserByVorname", findUserByVorname);

        JSONObject user = new JSONObject();
        user.put("id", 827);
        user.put("name", "Keßel");
        user.put("vorname", "Martin");
        user.put("email", "martin@kessel.de");
        user.put("_links", userLinks);
        return user;
    }

    private Schema createSchemaForUsers() {
        JSONObject embedded = new JSONObject();
        embedded.put("Users", createUsers());

        JSONObject first = new JSONObject();
        first.put("href", "http://localhost:8080/api/v1/users?page149&size=1&field=vorname&sortAlg=ASC");

        JSONObject prev = new JSONObject();
        prev.put("href", "http://localhost:8080/api/v1/users?page=149&size=5&sort=vorname,asc");

        JSONObject self = new JSONObject();
        self.put("href", "http://localhost:8080/api/v1/users?page=150&size=5&sort=vorname,asc");

        JSONObject next = new JSONObject();
        next.put("href", "http://localhost:8080/api/v1/users?page=151&size=5&sort=vorname,asc");

        JSONObject last = new JSONObject();
        last.put("href", "http://localhost:8080/api/v1/users?page=199&size=5&sort=vorname,asc");

        JSONObject saveUser = new JSONObject();
        saveUser.put("href", "http://localhost:8080/api/v1/user");
        saveUser.put("type", "POST");

        JSONObject page = new JSONObject();;
        page.put("size", 5);
        page.put("totalElement", 1000);
        page.put("totalPage", 200);
        page.put("number", 150);

        JSONObject links = new JSONObject();
        links.put("first", first);
        links.put("prev", prev);
        links.put("self", self);
        links.put("next", next);
        links.put("last", last);
        links.put("saveUser", saveUser);

        return new Schema<Map<String, Object>>()
                .addProperty("_embedded", new JsonSchema().example(embedded.toString()))
                .addProperty("_links", new JsonSchema().example(links.toString()))
                .addProperty("page", new JsonSchema().example(page.toString()));
    }

    private JSONObject createUpdateUserRequestBody() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id",1);
        requestBody.put("name","Keßel");
        requestBody.put("vorname","Martin");
        requestBody.put("email", "martinkessel@web.de");
        return requestBody;
    }
}
