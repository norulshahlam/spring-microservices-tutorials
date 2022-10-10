package com.shah.userservice.service;

import com.shah.userservice.VO.Department;
import com.shah.userservice.VO.ResponseTemplateVO;
import com.shah.userservice.entity.Users;
import com.shah.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Users saveUser(Users users) {
        log.info("Inside saveUser of UserService");
        return userRepository.save(users);
    }

    public ResponseTemplateVO getUserWithDepartment(Long userId) {
        log.info("Inside getUserWithDepartment of UserService");
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Users users = userRepository.findByUserId(userId);

        /*
        With service registry, you can use service name instead
        String deptServiceHostname = "http://localhost:9001";
        */
        String deptServiceHostname = "http://DEPARTMENT-SERVICE";
        Department department =
                restTemplate.getForObject(deptServiceHostname + "/departments/" + users.getDepartmentId()
                        , Department.class);

        vo.setUsers(users);
        vo.setDepartment(department);

        return vo;
    }
}
