package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.quantumsoft.hrms.Human_Resource_Website.enums.Mode;
import com.quantumsoft.hrms.Human_Resource_Website.enums.WorkFrom;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockInRequest {

    private Long employeeId;
    private WorkFrom workFrom;
    private Mode mode;
    private Double latitude;
    private Double longitude;

}
