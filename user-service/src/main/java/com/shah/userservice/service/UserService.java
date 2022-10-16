package com.shah.userservice.service;

import com.shah.userservice.VO.Department;
import com.shah.userservice.VO.ResponseTemplateVO;
import com.shah.userservice.entity.Users;
import com.shah.userservice.model.StatusCheckResponse;
import com.shah.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    public Users saveUser(Users users) {
        log.info("Inside saveUser of UserService");
        return userRepository.save(users);
    }

    public ResponseTemplateVO getUserWithDepartment(Long userId) {
        log.info("Inside getUserWithDepartment of UserService");
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Users user = userRepository.findByUserId(userId);

        vo.setUsers(user);
        if (ObjectUtils.isEmpty(user))
            return vo;
        log.info("User found: {}", user);

        /*
        With service registry, you can use service name instead
        String deptServiceHostname = "http://localhost:9001";
        */
        String deptServiceHostname = "http://DEPARTMENT-SERVICE";
        Department department =
                restTemplate.getForObject(deptServiceHostname + "/departments/" + user.getDepartmentId()
                        , Department.class);
        log.info("department found: {}", department);
        vo.setDepartment(department);
        return vo;
    }

    public List<Users> getAllUsers() {
        log.info("Inside getAllUsers of UserService");
        return userRepository.findAll();
    }

    public StatusCheckResponse statusCheck() {

        String instanceId = env.getProperty("eureka.instance.instance-id");
        String applicationName = env.getProperty("spring.application.name");
        String portNumber = env.getProperty("local.server.port");
        String appDescription = env.getProperty("app.description");

        log.info("Working from " + applicationName + " on port: " + portNumber + " and instance id: " + instanceId);

        return StatusCheckResponse
                .builder()
                .applicationName(applicationName)
                .instanceId(instanceId)
                .portNumber(portNumber)
                .timeStamp(ZonedDateTime.now())
                .appDescription(appDescription)
                .build();
    }
}
