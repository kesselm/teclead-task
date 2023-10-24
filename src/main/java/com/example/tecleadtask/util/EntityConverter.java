package com.example.tecleadtask.util;

import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityConverter {

    public static final User convertFromUserDTO(UserDTO userDTO){
        var user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.name());
        user.setVorname(userDTO.vorName());
        user.setEMail(userDTO.eMail());
        return user;
    }

    public static final UserDTO convertFromUserEntity(User user){
        return new UserDTO(user.getId(), user.getName(), user.getVorname(), user.getEMail());
    }
}
