package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quantumsoft.hrms.Human_Resource_Website.enums.ComplianceFrequency;
import com.quantumsoft.hrms.Human_Resource_Website.enums.ComplianceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "compliances")
public class Compliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complianceId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceFrequency frequency;

    private LocalDate dueDate;

    private String penalty;  // Text or numeric

    private Boolean documentRequired;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "emp_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Employee employee;

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

//    public boolean isEmployeeSpecific() {
//        return this.employee != null;
//    }
}
