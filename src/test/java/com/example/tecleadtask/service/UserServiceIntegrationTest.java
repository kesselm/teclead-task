package com.example.tecleadtask.service;

import com.example.tecleadtask.entities.User;
import com.example.tecleadtask.exception.UserAppException;
import com.example.tecleadtask.services.UserService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Testcontainers
public class UserServiceIntegrationTest {

    @Container
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15.2-alpine");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private UserService userService;

    @Test
    void saveExistingUserShouldThrowException() {
        User user = User.builder()
                .id(1L)
                .name("Doe")
                .vorname("John")
                .eMail("john@doe.com")
                .build();

        assertThatThrownBy(()-> {
            userService.saveUser(user);
        }).isInstanceOf(EntityExistsException.class)
                        .hasMessage("There is already existing entity with this id.");

    }

    @Test
    void saveUserSuccessfully() {
        User user = User.builder()
                .id(11L)
                .name("Keßel")
                .vorname("Martin")
                .eMail("martin@kessel.com")
                .build();

        String result = userService.saveUser(user).getName();
        assertThat(result).isEqualTo("Keßel");
    }

    @Test
    void saveUserWithoutGivenId() {
        User user = User.builder()
                .name("Doe")
                .vorname("Jane")
                .eMail("jane@doe.com")
                .build();

        String result = userService.saveUser(user).getName();
        assertThat(result).isEqualTo("Doe");
    }

    @Test
    void findAll12Users() {
        assertThat(userService.findAllUsers()).hasSize(11);
    }

    @Test
    void findOneUserById() {
        Optional<User> user = userService.findUserById(1L);
        assertThat(user).isPresent().isNotEmpty();
    }

    @Test
    void findUserByIdThrowExceptionByNullId() {
        assertThatThrownBy(()-> {
            userService.findUserById(null);
        }).isInstanceOf(UserAppException.class)
                .hasMessage("Id for user object is missing.");
    }

}
