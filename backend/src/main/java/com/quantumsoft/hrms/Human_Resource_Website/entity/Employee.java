package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.*;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Gender;
import com.quantumsoft.hrms.Human_Resource_Website.enums.JobType;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long empId;

    private String createdBy;

    @Column(unique = true, nullable = false)
    private String employeeCode;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(unique = true)
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
    private String contactNumber;

    @Column(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Emergency contact is required")
    @Pattern(regexp = "^\\d{10}$", message = "Emergency contact must be 10 digits")
    private String emergencyContact;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate probationEndDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignationDate;

    private String exitReason;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference("employee-department")
    private Department department;

    @Column(nullable = false)
    private String designation;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String location;

    @Lob
    private String education;

    @Lob
    private String experience;

    @Lob
    private String certifications;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference("employee-benefits")
    private List<EmployeeBenefit> benefits;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("employee-salary")
    private List<SalaryStructure> salaryStructures;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("employee-leaves")
    private List<Leave> leaves;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private BankDetail bankDetail;

    @OneToOne(mappedBy = "departmentHead")
    @JsonBackReference("department-head")
    private Department managedDepartments;
}

