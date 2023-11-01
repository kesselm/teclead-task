package com.example.tecleadtask.util;

import com.example.tecleadtask.dto.UserDTO;
import com.example.tecleadtask.entities.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityConverter {

    public static final UserEntity convertFromUserDTO(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setName(userDTO.getName());
        userEntity.setVorname(userDTO.getVorname());
        userEntity.setEMail(userDTO.getEMail());
        return userEntity;
    }


    public static final UserDTO convertFromUserEntity(UserEntity userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setName(userEntity.getName());
        userDTO.setVorname(userEntity.getVorname());
        userDTO.setEMail(userEntity.getEMail());
        return userDTO;
    }
}
