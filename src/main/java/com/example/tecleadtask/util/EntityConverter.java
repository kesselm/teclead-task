package com.example.tecleadtask.util;

import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityConverter {

    public static final User convertFromUserDTO(UserDTO userDTO){
        return User.builder()
                .id(userDTO.id())
                .name(userDTO.name())
                .vorname(userDTO.vorName())
                .eMail(userDTO.eMail())
                .build();
    }

    public static final UserDTO convertFromUserEntity(User user){
        return new UserDTO(user.getId(), user.getName(), user.getVorname(), user.getEMail());
    }
}
