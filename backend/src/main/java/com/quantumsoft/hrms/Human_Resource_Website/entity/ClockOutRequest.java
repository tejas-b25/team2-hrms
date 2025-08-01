package com.quantumsoft.hrms.Human_Resource_Website.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockOutRequest {
    private Long employeeId;
}
