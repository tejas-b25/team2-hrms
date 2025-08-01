package com.quantumsoft.hrms.Human_Resource_Website.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob
    @Column(length = 10485760) // e.g. 10MB max
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private PerformanceReview performanceReview;
}

