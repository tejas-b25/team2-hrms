package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;

@Entity
@Table
@Validated
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(unique = true)
    private  String username;

    @Column(unique = true, nullable = false)
    private String email;

    private  String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime lastlogin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
