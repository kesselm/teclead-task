package com.example.tecleadtask.service;

import com.example.tecleadtask.entities.User;
import com.example.tecleadtask.exception.UserAppException;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
        when(userRepositoryMock.findAll()).thenReturn(List.of(DummyUserEntity.createUserEntity()));

        List<User> result = sut.findAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Keßel");
    }

    @Test
    void getUserByIdTest() {
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(DummyUserEntity.createUserEntity()));

        assertThat(sut.findUserById(1L).get().getName()).isEqualTo("Keßel");
    }

    @Test
    void deleteUserTest(CapturedOutput output) {
        when(userRepositoryMock.existsById(any())).thenReturn(true);
        sut.deleteUser(DummyUserEntity.createUserEntity());

        assertThat(output.getOut()).contains("User with id:");
        verify(userRepositoryMock).delete(any());
    }

    @Test
    void deleteUserUnknownUserTest(CapturedOutput output) {
        when(userRepositoryMock.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> sut.deleteUser(DummyUserEntity.createUserEntity())).isInstanceOf(UserAppException.class)
                .hasMessage("No object to delete.");
        assertThat(output.getOut()).contains("could not be deleted.");
    }

    @Test
    void deleteUserByIdTest() {
        when(userRepositoryMock.existsById(any())).thenReturn(true);
        sut.deleteUserById(1L);
        verify(userRepositoryMock).deleteById(any());
    }

    @Test
    void deleteUserByIdUnknownObjectTest() {
        when(userRepositoryMock.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> {
            sut.deleteUserById(1L);
        }).isInstanceOf(UserAppException.class)
                .hasMessage("No object to delete.");
    }

    @Test
    void updateUserTest() {
        when(userRepositoryMock.existsById(any())).thenReturn(true);
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(DummyUserEntity.createUserEntity()));
        when(userRepositoryMock.save(any())).thenReturn(DummyUserEntity.createUserEntity());

        User result = sut.updateUser(DummyUserEntity.createUserEntity());
        verify(userRepositoryMock).save(any());
        assertThat(result.getName()).isEqualTo(DummyUserEntity.createUserEntity().getName());
    }

    @Test
    void updateUserLoggingMessageTest(CapturedOutput output) {
        when(userRepositoryMock.existsById(any())).thenReturn(true);
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(DummyUserEntity.createUserEntity()));
        when(userRepositoryMock.save(any())).thenReturn(DummyUserEntity.createUserEntity());

        sut.updateUser(DummyUserEntity.createUserEntity());
        assertThat(output.getOut()).contains("User with id:");
    }

    @Test
    void updateUserUnknownObjectTest(CapturedOutput output) {
        when(userRepositoryMock.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> sut.updateUser(DummyUserEntity.createUserEntity())).isInstanceOf(UserAppException.class)
                .hasMessage("No object to update.");
        assertThat(output.getOut()).contains("could not be updated");
    }

    @Test
    void findByVornameTest() {
        when(userRepositoryMock.findByVorname("Martin"))
                .thenReturn(List.of(DummyUserEntity.createUserEntity()));
        String result = sut.findByVorname("Martin").get(0).getName();
        assertThat(result).isEqualTo("Keßel");
    }
}
