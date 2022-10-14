package com.shah.departmentservice.service;

import com.shah.departmentservice.entity.Department;
import com.shah.departmentservice.model.StatusCheckResponse;
import com.shah.departmentservice.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class DepartmentService {

    @Autowired
    private Environment env;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department saveDepartment(Department department) {
        log.info("Inside saveDepartment of DepartmentService");
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        log.info("Inside getAllDepartments of DepartmentService");
        return departmentRepository.findAll();
    }

    public Department findDepartmentById(Long departmentId) {
        log.info("Inside saveDepartment of DepartmentService");
        return departmentRepository.findByDepartmentId(departmentId);
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
