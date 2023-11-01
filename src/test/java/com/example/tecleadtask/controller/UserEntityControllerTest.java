package com.example.tecleadtask.controller;

import com.example.tecleadtask.controllers.UserController;
import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.exception.UserAppException;
import com.example.tecleadtask.services.UserService;
import com.example.tecleadtask.util.ApiConstants;
import com.example.tecleadtask.util.DummyUserEntity;
import com.example.tecleadtask.util.EntityConverter;
import com.example.tecleadtask.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityExistsException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserEntityControllerTest {

    @MockBean
    private UserService userServiceMock;

    private final WebApplicationContext context;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserEntityControllerTest(WebApplicationContext context) {
        this.context = context;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @Test
    @DisplayName("A user entity is persisted and response should be 'ok'.")
    void saveUserTest() throws Exception {

        when(userServiceMock.saveUser(any())).thenReturn(DummyUserEntity.createUserEntity());
        UserDTO userDTO = EntityConverter.convertFromUserEntity(DummyUserEntity.createUserEntity());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post(ApiConstants.SAVE_USER)
                        .content(mapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpectAll(
                        content().json("{'name':'Keßel'}")
                );
    }

    @Test
    @DisplayName("A user entity is persisted and response should be 'bad request'.")
    void saveUserValidationNoVorNameAttribute() throws Exception {

        var userDAO = new JSONObject();
        userDAO.put("name", "Keßel");
        userDAO.put("email", "info@example.de");

        mockMvc.perform(post(ApiConstants.SAVE_USER)
                        .content(userDAO.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("The attribute name is empty and the response should be 'bad request'.")
    void saveUserValidationEmptyVornameField() throws Exception {
        var userDAO = new UserDTO();
        userDAO.setId(1L);
        userDAO.setName("Keßel");
        userDAO.setVorname("");
        userDAO.setEMail("info@example.com");

        mockMvc.perform(post(ApiConstants.SAVE_USER)
                        .content(TestUtil.asJsonString(userDAO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("The attribute email is empty and the response should be 'created'.")
    void saveUserValidationEmptyEmailField() throws Exception {
        var userDAO = new UserDTO();
        userDAO.setId(1L);
        userDAO.setName("Keßel");
        userDAO.setVorname("Martin");
        userDAO.setEMail("");

        when(userServiceMock.saveUser(any())).thenReturn(DummyUserEntity.createUserEntity());

        mockMvc.perform(post(ApiConstants.SAVE_USER)
                        .content(TestUtil.asJsonString(userDAO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("The email address is wrong written and the response should be 'bad request'.")
    void saveUserValidationWrongEmailField() throws Exception {
        var userDAO = new UserDTO();
        userDAO.setId(1L);
        userDAO.setName("Keßel");
        userDAO.setVorname("Martin");
        userDAO.setEMail("example.com");

        mockMvc.perform(post(ApiConstants.SAVE_USER)
                        .content(TestUtil.asJsonString(userDAO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Email attribute does not exist and the response should be 'created'.")
    void saveUserValidationNoEmailAttribute() throws Exception {

        var userDAO = new JSONObject();
        userDAO.put("name", "Keßel");
        userDAO.put("vorname", "Martin");

        when(userServiceMock.saveUser(any())).thenReturn(DummyUserEntity.createUserEntity());

        mockMvc.perform(post(ApiConstants.SAVE_USER)
                        .content(userDAO.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("User entity with the same id is known, response should be 'server error'.")
    void saveUserWithKnownId() throws Exception {

        var userDAO = new JSONObject();
        userDAO.put("id", "1");
        userDAO.put("name", "Doe");
        userDAO.put("vorname", "Jane");

        when(userServiceMock.saveUser(any())).thenThrow(new EntityExistsException(""));

        mockMvc.perform(post(ApiConstants.SAVE_USER)
                        .content(userDAO.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Response should be a List of users.")
    void findAllUsersTest() throws Exception {
        when(userServiceMock.findAllUsers()).thenReturn(List.of(DummyUserEntity.createUserEntity()));

        mockMvc.perform(get(ApiConstants.GET_USERS))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        content().json("[{'name':'Keßel'}]")
                );
    }

    @Test
    @DisplayName("Empty list should return response status 'no content'.")
    void findAllUsersFromEmptyList() throws Exception {
        when(userServiceMock.findAllUsers()).thenReturn(List.of());

        mockMvc.perform(get(ApiConstants.GET_USERS))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Response should return an user entity for a given id.")
    void findUserById() throws Exception {
        when(userServiceMock.findUserById(any())).thenReturn(Optional.of(DummyUserEntity.createUserEntity()));

        mockMvc.perform(get(ApiConstants.FIND_USER_BY_ID, 1))
                .andExpect(status().isOk())
                .andExpectAll(
                        content().json("{'name':'Keßel'}")
                );
    }

    @Test
    @DisplayName("Response should return 'no content'.")
    void findUserByIdEmptyResult() throws Exception {
        when(userServiceMock.findUserById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get(ApiConstants.FIND_USER_BY_ID, 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Response should return 'Server Error.")
    void findUserByIdThrowException() throws Exception {
        doThrow(new UserAppException("Id for user object is missing."))
                .when(userServiceMock)
                .findUserById(any());

        mockMvc.perform(get(ApiConstants.FIND_USER_BY_ID,1))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Response should be 'ok'.")
    void deleteUserSuccessful() throws Exception {
        UserDTO userDTO = EntityConverter.convertFromUserEntity(DummyUserEntity.createUserEntity());

        doNothing().when(userServiceMock).deleteUser(any());

        mockMvc.perform(delete(ApiConstants.DELETE_USER)
                        .content(mapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("No user to delete, response should be 'no content'.")
    void deleteUserWithUnknownUser() throws Exception {

        var userDTO = new JSONObject();
        userDTO.put("name", "bang");
        userDTO.put("vorname", "Farid");
        userDTO.put("email", "Farid");

        doThrow(new UserAppException("No object to delete.")).when(userServiceMock).deleteUser(any());

        mockMvc.perform(delete(ApiConstants.DELETE_USER)
                        .content(userDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Response should be 'is ok'.")
    void deleteUserById() throws Exception {
        UserDTO userDTO = EntityConverter.convertFromUserEntity(DummyUserEntity.createUserEntity());

        doNothing().when(userServiceMock).deleteUser(any());

        mockMvc.perform(delete(ApiConstants.DELETE_USER_BY_ID, 1)
                        .content(mapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserByIdUnknownUser() throws Exception {
        UserDTO userDTO = EntityConverter.convertFromUserEntity(DummyUserEntity.createUserEntity());

        doThrow(new UserAppException("")).when(userServiceMock).deleteUserById(any());

        mockMvc.perform(delete(ApiConstants.DELETE_USER_BY_ID, 1)
                        .content(mapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

    }

    @Test
    @DisplayName("User should be updated and the response should be 'ok'.")
    void updateUserTest() throws Exception {
        UserDTO userDTO = EntityConverter.convertFromUserEntity(DummyUserEntity.createUserEntity());

        when(userServiceMock.updateUser(any())).thenReturn(DummyUserEntity.createUserEntity());

        mockMvc.perform(put(ApiConstants.UPDATE_USER)
                        .content(mapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("No user object could be updated and the response should be 'no content'.")
    void updateUserWithEmptyObject() throws Exception {
        UserDTO userDTO = EntityConverter.convertFromUserEntity(DummyUserEntity.createUserEntity());

        doThrow(new UserAppException("")).when(userServiceMock).updateUser(any());

        mockMvc.perform(put(ApiConstants.UPDATE_USER)
                        .content(mapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("User is found by vorname and the response should be 'ok'.")
    void findUserByVornameTest() throws Exception {

        when(userServiceMock.findByVorname("Martin"))
                .thenReturn(List.of(DummyUserEntity.createUserEntity()));

        mockMvc.perform(get(ApiConstants.FIND_USER_BY_VORNAME).param("vorname", "Martin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        content().json("[{'name':'Keßel'}]")
                );
    }

    @Test
    @DisplayName("User could not be found by vorname and the response should be 'no content'.")
    void couldNotFindUserByNameTest() throws Exception {

        when(userServiceMock.findByVorname("Martin"))
                .thenReturn(List.of());

        mockMvc.perform(get(ApiConstants.FIND_USER_BY_VORNAME).param("vorname", "Martin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
