package com.shah.userservice.startup;

import com.shah.userservice.entity.Users;
import com.shah.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository repository;
    
    @Override
    public void run(String... args) throws Exception {

        log.info("Adding user on start..");
        Users user = Users.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john_doe@email.com")
                .departmentId(33L)
                .build();
        Users savedUser = repository.save(user);
        log.info("user saved: {}",savedUser);
    }
}
