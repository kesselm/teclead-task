package com.example.tecleadtask.services;

import com.example.tecleadtask.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity saveUser(UserEntity userEntity);
    List<UserEntity> findAllUsers();
    Optional<UserEntity> findUserById(Long id);
    void deleteUser(UserEntity userEntity);
    void deleteUserById(Long id);
    UserEntity updateUser(UserEntity userEntity);
    List<UserEntity> findByVorname(String vorname);
}
