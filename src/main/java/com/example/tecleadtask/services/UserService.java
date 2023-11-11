package com.example.tecleadtask.services;

import com.example.tecleadtask.entities.UserEntity;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity saveUser(UserEntity userEntity);
    Page<UserEntity> findAllUsersWithPagination(int page, int size, Sort sort);
    List<UserEntity> findAllUsers();
    Optional<UserEntity> findUserById(Long id);
    void deleteUser(UserEntity userEntity);
    void deleteUserById(Long id);
    UserEntity updateUser(UserEntity userEntity);
    List<UserEntity> findByVorname(String vorname);
}
