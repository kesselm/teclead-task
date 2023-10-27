package com.example.tecleadtask.services;

import com.example.tecleadtask.entities.User;
import com.example.tecleadtask.exception.UserAppException;
import com.example.tecleadtask.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.tecleadtask.util.ApiConstants.CUSTOM_MESSAGE;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            throw new EntityExistsException("There is already existing entity with this id.");
        }
        User newUser = userRepository.save(user);
        log.info("{} New user with id: {} is persisted.", CUSTOM_MESSAGE, newUser.getId());
        return newUser;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        if (id == null) {
            throw new UserAppException("Id for user object is missing.");
        } else {
            return userRepository.findById(id);
        }
    }

    @Override
    public void deleteUser(User user) {
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            userRepository.delete(user);
            log.info("{} User with id: {} was deleted.", CUSTOM_MESSAGE, user.getId());
        } else {
            log.info("{} User {} could not be deleted.", CUSTOM_MESSAGE, user);
            throw new UserAppException("No object to delete.");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        if (id != null && userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("{} User with id: {} was deleted.", CUSTOM_MESSAGE, id);
        } else {
            throw new UserAppException("No object to delete.");
        }
    }

    @Override
    public User updateUser(User user) {
        User newUser = new User();
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            Optional<User> userOptional = userRepository.findById(user.getId());
            if (userOptional.isPresent()) {
                User oldUser = userOptional.get();
                oldUser.setId(user.getId());
                oldUser.setName(user.getName() != null ? user.getName() : oldUser.getName());
                oldUser.setVorname(user.getVorname() != null ? user.getVorname() : oldUser.getVorname());
                oldUser.setEMail(user.getEMail() != null ? user.getEMail() : oldUser.getEMail());
                newUser = userRepository.save(oldUser);
                log.info("{} User with id: {} is updated.", CUSTOM_MESSAGE, user.getId());
            }
        } else {
            log.info("{} User {} could not be updated.", CUSTOM_MESSAGE, user);
            throw new UserAppException("No object to update.");
        }
        return newUser;
    }

    @Override
    public List<User> findByVorname(String vorname) {
        return userRepository.findByVorname(vorname);
    }
}
