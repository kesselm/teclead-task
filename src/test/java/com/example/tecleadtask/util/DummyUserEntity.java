package com.example.tecleadtask.util;

import com.example.tecleadtask.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DummyUserEntity {

    public static final User createUserEntity() {
        User user = new User();
        user.setId(1L);
        user.setName("Keßel");
        user.setVorname("Martin");
        user.setEMail("info@example");
        return user;
    }
}
