package com.example.tecleadtask.services;

import com.example.tecleadtask.entities.User;
import com.example.tecleadtask.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.tecleadtask.util.ApiConstants.CUSTOM_ERROR_IDENTIFIER;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        User newUser = userRepository.save(user);
        log.info("{} New user with id: {} is persisted.", CUSTOM_ERROR_IDENTIFIER, newUser.getId());
        return newUser;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
        log.info("{} User with id: {} was deleted.", CUSTOM_ERROR_IDENTIFIER, user.getId());
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        log.info("{} User with id: {} was deleted.", CUSTOM_ERROR_IDENTIFIER, id);
    }

    @Override
    public List<User> findByVorname(String vorname) {
        return userRepository.findByVorname(vorname);
    }
}
