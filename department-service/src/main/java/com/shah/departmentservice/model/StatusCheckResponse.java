package com.shah.departmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusCheckResponse {

    private String applicationName;
    private String portNumber;
    private String instanceId;
    private ZonedDateTime timeStamp;
    private String appDescription;
}
