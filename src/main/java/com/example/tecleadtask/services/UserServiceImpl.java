package com.example.tecleadtask.services;

import com.example.tecleadtask.entities.UserEntity;
import com.example.tecleadtask.exception.UserAppException;
import com.example.tecleadtask.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.tecleadtask.util.ApiConstants.CUSTOM_MESSAGE;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        if (userEntity.getId() != null && userRepository.existsById(userEntity.getId())) {
            throw new EntityExistsException("There is already existing entity with this id.");
        }
        UserEntity newUserEntity = userRepository.save(userEntity);
        log.info("{} New user with id: {} is persisted.", CUSTOM_MESSAGE, newUserEntity.getId());
        return newUserEntity;
    }

    @Override
    public Page<UserEntity> findAllUsersWithPagination(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public Optional<UserEntity> findUserById(Long id) {
        if (id == null) {
            throw new UserAppException("Id for user object is missing.");
        } else {
            return userRepository.findById(id);
        }
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        if (userEntity.getId() != null && userRepository.existsById(userEntity.getId())) {
            userRepository.delete(userEntity);
            log.info("{} User with id: {} was deleted.", CUSTOM_MESSAGE, userEntity.getId());
        } else {
            log.info("{} User {} could not be deleted.", CUSTOM_MESSAGE, userEntity);
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
    public UserEntity updateUser(UserEntity userEntity) {
        UserEntity newUserEntity = new UserEntity();
        if (userEntity.getId() != null && userRepository.existsById(userEntity.getId())) {
            Optional<UserEntity> userOptional = userRepository.findById(userEntity.getId());
            if (userOptional.isPresent()) {
                UserEntity oldUserEntity = userOptional.get();
                oldUserEntity.setId(userEntity.getId());
                oldUserEntity.setName(userEntity.getName() != null ? userEntity.getName() : oldUserEntity.getName());
                oldUserEntity.setVorname(userEntity.getVorname() != null ? userEntity.getVorname() : oldUserEntity.getVorname());
                oldUserEntity.setEMail(userEntity.getEMail() != null ? userEntity.getEMail() : oldUserEntity.getEMail());
                newUserEntity = userRepository.save(oldUserEntity);
                log.info("{} User with id: {} is updated.", CUSTOM_MESSAGE, userEntity.getId());
            }
        } else {
            log.info("{} User {} could not be updated.", CUSTOM_MESSAGE, userEntity);
            throw new UserAppException("No object to update.");
        }
        return newUserEntity;
    }

    @Override
    public List<UserEntity> findByVorname(String vorname) {
        return userRepository.findByVorname(vorname);
    }
}
