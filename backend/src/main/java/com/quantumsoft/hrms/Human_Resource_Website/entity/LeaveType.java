package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveTypeId;

    private String name; // Sick, Casual, Earned

    @Column(name = "carry_forward")
    private boolean carryForward;

    @Column(name = "encashable")
    private boolean encashable;

    @Column(name = "max_per_year")
    private int maxDaysPerYear;

    @OneToMany(mappedBy = "leaveType")
    private List<Leave> leaves;

    @Column(name = "approval_flow")
    private String approvalFlow;

    @Column(name="description")
    private  String description;


}
