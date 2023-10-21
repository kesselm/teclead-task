package com.example.tecleadtask.service;

import com.example.tecleadtask.entities.User;
import com.example.tecleadtask.repositories.UserRepository;
import com.example.tecleadtask.services.UserService;
import com.example.tecleadtask.services.UserServiceImpl;
import com.example.tecleadtask.util.DummyUserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepositoryMock;

    private UserService sut;

    @BeforeEach
    void setUp() {
        sut = new UserServiceImpl(userRepositoryMock);
    }

    @Test
    void saveUserTest() {
        when(userRepositoryMock.save(any())).thenReturn(DummyUserEntity.createUserEntity());

        User result = sut.saveUser(DummyUserEntity.createUserEntity());

        assertThat(result.getName()).isEqualTo("Keßel");
    }

    @Test
    void saverUserLoggingMessageTest(CapturedOutput output) {
        when(userRepositoryMock.save(any())).thenReturn(DummyUserEntity.createUserEntity());

        sut.saveUser(DummyUserEntity.createUserEntity());

        assertThat(output.getOut()).contains("New user");
    }

    @Test
    void findAllUsersTest() {
        when(userRepositoryMock.findAll()).thenReturn(Arrays.asList(DummyUserEntity.createUserEntity()));

        List<User> result = sut.findAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Keßel");
    }

    @Test
    void getUserByIdTest() {
        when(userRepositoryMock.findById(any())).thenReturn(Optional.ofNullable(DummyUserEntity.createUserEntity()));
        assertThat(sut.findUserById(1L).get().getName()).isEqualTo("Keßel");
    }

    @Test
    void deleteUserTest(CapturedOutput output) {
        sut.deleteUser(DummyUserEntity.createUserEntity());
        assertThat(output.getOut()).contains("User with id:");
        verify(userRepositoryMock).delete(any());
    }

    @Test
    void deleteUserByIdTest() {
        sut.deleteUserById(1L);
        verify(userRepositoryMock).deleteById(any());
    }

    @Test
    void findByVornameTest() {
        when(userRepositoryMock.findByVorname("Martin"))
                .thenReturn(List.of(DummyUserEntity.createUserEntity()));
        String result = sut.findByVorname("Martin").get(0).getName();
        assertThat(result).isEqualTo("Keßel");
    }
}
