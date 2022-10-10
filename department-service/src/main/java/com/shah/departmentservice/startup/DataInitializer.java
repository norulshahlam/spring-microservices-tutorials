package com.shah.departmentservice.startup;

import com.shah.departmentservice.entity.Department;
import com.shah.departmentservice.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository repository;
    
    @Override
    public void run(String... args) throws Exception {

        log.info("Adding dept on start..");
        Department dept = Department.builder()
                .departmentId(33L)
                .departmentName("Software Engineering")
                .departmentAddress("Jurong West st 42")
                .departmentCode("SOFT-JW01")
                .build();

        Department savedDept = repository.save(dept);
        log.info("dept saved: {}",savedDept);
    }
}
