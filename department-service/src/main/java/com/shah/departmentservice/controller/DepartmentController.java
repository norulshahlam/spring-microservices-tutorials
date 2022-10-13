package com.shah.departmentservice.controller;

import com.shah.departmentservice.entity.Department;
import com.shah.departmentservice.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

    @Autowired
    private Environment env;

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/")
    public Department saveDepartment(@RequestBody Department department) {
        log.info("Inside saveDepartment method of DepartmentController");
        return departmentService.saveDepartment(department);
    }

    @GetMapping("/")
    public List<Department> getAllDepartments() {
        log.info("Inside saveDepartment method of DepartmentController");
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department findDepartmentById(@PathVariable("id") Long departmentId) {
        log.info("Inside findDepartmentById method of DepartmentController");
        return departmentService.findDepartmentById(departmentId);
    }

    @GetMapping("/status/check")
    public String status() {
        log.info("instance id: {}", env.getProperty("eureka.instance.instance-id"));
        return "Working from " + env.getProperty("spring.application.name") + " on port: " + env.getProperty("local.server.port") + " and instance id: "
                + env.getProperty("eureka.instance.instance-id");
    }

}
