package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    @JsonIgnore
    private Employee employee;

    @Column(nullable = false)
    private String reviewCycle; // e.g. "2025-Q2"

    @Column(length = 2000, columnDefinition = "TEXT")
    private String selfAssessment;

    @Column(length = 2000, columnDefinition = "TEXT")
    private String managerReview;

    @Column(length = 2000, columnDefinition = "TEXT")
    private String peerReview;

    private Double overallScore;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String goals; // store JSON string (KRA/KPI with score weightages etc.)

    private boolean finalized;

    @OneToMany(mappedBy = "performanceReview", cascade = CascadeType.ALL)
    private List<ReviewAttachment> attachments;
}
