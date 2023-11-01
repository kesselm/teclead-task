package com.example.tecleadtask.util;

import com.example.tecleadtask.entities.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DummyUserEntity {

    public static final UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Keßel");
        userEntity.setVorname("Martin");
        userEntity.setEMail("info@example");
        return userEntity;
    }
}
