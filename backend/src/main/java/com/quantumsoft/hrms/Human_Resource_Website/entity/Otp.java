    package com.quantumsoft.hrms.Human_Resource_Website.entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.validation.annotation.Validated;

    import java.time.Instant;
    import java.time.LocalDateTime;


    @Entity
    @Table
    @Getter
    @Setter
    @Validated
    public class Otp {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String email;

        private  String otp;

        private LocalDateTime expiry;

    }
