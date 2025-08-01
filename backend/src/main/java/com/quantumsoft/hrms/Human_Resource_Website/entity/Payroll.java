package com.quantumsoft.hrms.Human_Resource_Website.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@Table(name="payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "salary_structure_id")
//    private SalaryStructure salaryStructure;

    private String month;

    private int year;

    private LocalDate generatedDate;

    private double totalEarnings;
    private double totalDeductions;
    private double netSalary;

    private String paymentStatus;

    private LocalDate paymentDate;

    private long presentDays;

    private long paidLeaveDays;

    private long workingDays;

    private double payableDays;

    private long arrearDays;

    public Payroll(Long id, Employee employee, String month, int year, LocalDate generatedDate, double totalEarnings, double totalDeductions, double netSalary, String paymentStatus, LocalDate paymentDate, long presentDays, long paidLeaveDays, long workingDays, double payableDays, long arrearDays) {
        this.id = id;
        this.employee = employee;
        this.month = month;
        this.year = year;
        this.generatedDate = generatedDate;
        this.totalEarnings = totalEarnings;
        this.totalDeductions = totalDeductions;
        this.netSalary = netSalary;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.presentDays = presentDays;
        this.paidLeaveDays = paidLeaveDays;
        this.workingDays = workingDays;
        this.payableDays = payableDays;
        this.arrearDays = arrearDays;
    }

    public Payroll() {
    }
}
