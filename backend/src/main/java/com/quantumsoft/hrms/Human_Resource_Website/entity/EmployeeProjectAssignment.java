package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AssignmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class EmployeeProjectAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String role;

    private LocalDate assignedDate;

    private LocalDate releaseDate;

    private Integer allocationPercentage;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;
}
