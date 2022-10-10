package com.shah.departmentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Department {

    @Id
    private Long departmentId;
    private String departmentName;
    private String departmentAddress;
    private String departmentCode;
}
