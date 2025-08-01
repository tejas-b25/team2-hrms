package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AttendanceStatus;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Mode;
import com.quantumsoft.hrms.Human_Resource_Website.enums.RegularizationStatus;
import com.quantumsoft.hrms.Human_Resource_Website.enums.WorkFrom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="attendance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    private LocalDate date;

    private LocalTime clockInTime;

    private LocalTime clockOutTime;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    @Enumerated(EnumType.STRING)
    private WorkFrom workFrom;

    private Double latitude;

    private Double longitude;

    @Column(name = "total_hours_worked")
    private Double totalHoursWorked;

    private String timesheetProjectCode;  // nullable

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AttendanceStatus status;

    @Enumerated(EnumType.STRING)
    private RegularizationStatus regularizationStatus; // PENDING, APPROVED, REJECTED

    private String regularizationReason;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime approvedClockInTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime approvedClockOutTime;
}
