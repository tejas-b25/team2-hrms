package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quantumsoft.hrms.Human_Resource_Website.enums.RecordStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "compliance_records")
public class ComplianceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compliance_id", nullable = false)
    private Compliance compliance;

    @Column(nullable = false)
    private String month; // Example: "April 2025"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordStatus status; // PENDING / COMPLETED / OVERDUE

    private String filePath; // Stored path for PDF/JPG

    private LocalDate submittedOn;

    private String remarks;

    private String createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
