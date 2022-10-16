package com.shah.userservice.controller;

import com.shah.userservice.VO.ResponseTemplateVO;
import com.shah.userservice.entity.Users;
import com.shah.userservice.model.StatusCheckResponse;
import com.shah.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @PostMapping("/")
    public Users saveUser(@RequestBody Users users) {
        log.info("Inside saveUser of UserController");
        return userService.saveUser(users);
    }

    @GetMapping("/")
    public List<Users> getAllUsers() {
        log.info("Inside saveUser of UserController");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseTemplateVO getUserWithDepartment(@PathVariable("id") Long userId) {
        log.info("Inside getUserWithDepartment of UserController");
        return userService.getUserWithDepartment(userId);
    }

    @GetMapping("/status-check")
    public StatusCheckResponse statusCheck() {
        return userService.statusCheck();
    }

}
