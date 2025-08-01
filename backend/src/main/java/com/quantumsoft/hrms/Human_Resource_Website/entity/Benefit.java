package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.quantumsoft.hrms.Human_Resource_Website.enums.BenefitType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@Entity
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitId;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private BenefitType type;

    private Boolean isTaxable;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "benefit")
    @JsonManagedReference
    private List<EmployeeBenefit> employeeBenefits;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
