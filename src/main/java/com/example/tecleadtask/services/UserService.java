package com.example.tecleadtask.services;

import com.example.tecleadtask.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    void deleteUser(User user);
    void deleteUserById(Long id);
    User updateUser(User user);
    List<User> findByVorname(String vorname);
}
