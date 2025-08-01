package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class SalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn( name="emp_id")
    @JsonBackReference("employee-salary")
    private Employee employee;

    private Double basicSalary;

    private Double hra;

    private Double specialAllowance;

    private Double bonus;

    private Double pfDeduction;

    private Double taxDeduction;

    private LocalDate effectiveFrom;
    
    private LocalDate effectiveTo;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime  createdDate;

    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}
