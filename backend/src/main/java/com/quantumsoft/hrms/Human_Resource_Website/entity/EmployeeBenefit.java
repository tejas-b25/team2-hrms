package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table
@Data
@Entity
public class EmployeeBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeBenefitId;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    @JsonBackReference("employee-benefits")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "benefit_id")
    @JsonBackReference
    private Benefit benefit;

    private Double amount;

    private LocalDateTime effectiveFrom;

    private LocalDateTime effectiveTo;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String notes;

    private LocalDateTime enrollmentDate;

    @PrePersist
    public void prePersist() {
        enrollmentDate = LocalDateTime.now();
    }
}
