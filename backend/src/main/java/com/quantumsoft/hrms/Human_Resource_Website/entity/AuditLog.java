package com.quantumsoft.hrms.Human_Resource_Website.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String action;    // LOGIN, LOGOUT, PASSWORD_CHANGE etc.

//    private String ipAddress; // Optional

    private LocalDateTime timestamp;
}
