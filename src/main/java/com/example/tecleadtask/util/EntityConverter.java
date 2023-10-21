package com.example.tecleadtask.util;

import com.example.tecleadtask.dao.UserDAO;
import com.example.tecleadtask.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityConverter {

    public static final User convertFromUserDAO(UserDAO userDAO){
        var user = new User();
        user.setId(userDAO.id());
        user.setName(userDAO.name());
        user.setVorname(userDAO.vorName());
        user.setEMail(userDAO.eMail());
        return user;
    }

    public static final UserDAO convertFromUserEntity(User user){
        return new UserDAO(user.getId(), user.getName(), user.getVorname(), user.getEMail());
    }
}
