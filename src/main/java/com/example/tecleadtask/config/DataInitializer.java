package com.example.tecleadtask.config;

import com.example.tecleadtask.entities.UserEntity;
import com.example.tecleadtask.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
            var user1 = new UserEntity();
            user1.setName("Doe");
            user1.setVorname("John");
            user1.setEMail("john@doe.de");

            var user2 = new UserEntity();
            user2.setName("Keßel");
            user2.setVorname("Martin");
            user2.setEMail("martin@kessel.de");

            var user3 = new UserEntity();
            user3.setName("Müller");
            user3.setVorname("Martin");
            user3.setEMail("martin@mueller.de");

            var user4 = new UserEntity();
            user4.setName("Schmidt");
            user4.setVorname("Claudia");
            user4.setEMail("claudia@schmidt.de");

            var user5 = new UserEntity();
            user5.setName("Müller");
            user5.setVorname("Claudia");
            user5.setEMail("claudia@mueller.de");

            var user6 = new UserEntity();
            user6.setName("Meißner");
            user6.setVorname("Renate");
            user6.setEMail("renate@Meissner.de");

            var user7 = new UserEntity();
            user7.setName("Anders");
            user7.setVorname("Renate");
            user7.setEMail("renate@Andersch.de");

            var user8 = new UserEntity();
            user8.setName("Sieg");
            user8.setVorname("Renate");
            user8.setEMail("renate@Sieg.de");

            var user9 = new UserEntity();
            user9.setName("Borchert");
            user9.setVorname("Dirk");
            user9.setEMail("dirk@Borchert.com");

            var user10 = new UserEntity();
            user10.setName("Teichert");
            user10.setVorname("Holger");
            user10.setEMail("holger@teichert.de");

            List<UserEntity> userEntities = List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10);
            userRepository.saveAll(userEntities);
    }
}
