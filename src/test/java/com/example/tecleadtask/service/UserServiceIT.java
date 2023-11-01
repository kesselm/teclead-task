package com.example.tecleadtask.service;

import com.example.tecleadtask.entities.UserEntity;
import com.example.tecleadtask.exception.UserAppException;
import com.example.tecleadtask.services.UserService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Order;
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
class UserServiceIT {

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
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Doe");
        userEntity.setVorname("John");
        userEntity.setEMail("john@doe.com");

        assertThatThrownBy(() -> userService.saveUser(userEntity)).isInstanceOf(EntityExistsException.class)
                .hasMessage("There is already existing entity with this id.");

    }

    @Test
    @Order(1)
    void saveUserSuccessfully() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(12L);
        userEntity.setName("Keßel");
        userEntity.setVorname("Martin");
        userEntity.setEMail("john@doe.com");

        String result = userService.saveUser(userEntity).getName();
        assertThat(result).isEqualTo("Keßel");
    }

    @Test
    @Order(2)
    void saveUserWithoutGivenId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Keßel");
        userEntity.setVorname("Martin");
        userEntity.setEMail("john@doe.com");

        String result = userService.saveUser(userEntity).getName();
        assertThat(result).isEqualTo("Keßel");
    }

    @Test
    @Order(3)
    void findAll12Users() {
        assertThat(userService.findAllUsers()).hasSize(10);
    }

    @Test
    @Order(4)
    void deleteUserSuccessfully() {
        UserEntity user4 = new UserEntity();
        user4.setId(4L);
        user4.setName("Schmidt");
        user4.setVorname("claudia");
        user4.setEMail("claudia@gmail.com");

        userService.deleteUser(user4);
        assertThat(userService.findAllUsers()).hasSize(9);
    }

    @Test
    void findOneUserById() {
        Optional<UserEntity> userEntity = userService.findUserById(1L);
        assertThat(userEntity).isPresent().isNotEmpty();
    }

    @Test
    void findUserByIdThrowExceptionByNullId() {
        assertThatThrownBy(() -> userService.findUserById(null)).isInstanceOf(UserAppException.class)
                .hasMessage("Id for user object is missing.");
    }

    @Test
    void deleteUserOfUnknownUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Bang");
        userEntity.setVorname("Farid");
        userEntity.setEMail("farid@bang.de");

        assertThatThrownBy(() -> userService.deleteUser(userEntity)).isInstanceOf(UserAppException.class)
                .hasMessage("No object to delete.");
    }

}
