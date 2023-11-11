package com.example.tecleadtask.config;

import com.example.tecleadtask.entities.UserEntity;
import com.example.tecleadtask.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements ApplicationRunner {

    @Value("classpath:names.txt")
    private Resource names;

    @Value("classpath:firstNames.txt")
    private Resource firstNames;

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {

        List<UserEntity> userEntityList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String vorname = getRandomValue(getVornameList());
            String name = getRandomValue(getNameList());
            var user = new UserEntity();
            user.setName(name);
            user.setVorname(vorname);
            user.setEMail(vorname + "@" + name + ".de");
            userEntityList.add(user);
        }

        userRepository.saveAll(userEntityList);
    }

    private String getRandomValue(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    private List<String> getVornameList() throws IOException {
        return Files.readAllLines(Path.of(firstNames.getFile().getPath()));
    }

    private List<String> getNameList() throws IOException {
        return Files.readAllLines(Path.of(names.getFile().getPath()));
    }

}
