package com.quantumsoft.hrms.Human_Resource_Website.controller;


import com.quantumsoft.hrms.Human_Resource_Website.entity.LeaveType;
import com.quantumsoft.hrms.Human_Resource_Website.service.LeaveTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/leave-types")
@Tag(name="LeaveType")
public class LeaveTypeController {
    @Autowired
    private LeaveTypeService leaveTypeService;

    @PreAuthorize("ADMIN/HR")
    @PostMapping("/create")
    public ResponseEntity<LeaveType> createLeaveType(@RequestBody LeaveType leaveType) {
        return ResponseEntity.ok(leaveTypeService.createLeaveType(leaveType));
    }
    @GetMapping("/all")
    public ResponseEntity<List<LeaveType>> getAllLeaveTypes() {
        return ResponseEntity.ok(leaveTypeService.getAllLeaveTypes());
    }
    @GetMapping("/get/{leaveTypeId}")
    public ResponseEntity<LeaveType> getLeaveTypeById(@PathVariable Long leaveTypeId) {
        return ResponseEntity.ok(leaveTypeService.getLeaveTypeById(leaveTypeId));
    }
    @PutMapping("/update/{leaveTypeId}")
    public ResponseEntity<LeaveType> updateLeaveType(@PathVariable Long leaveTypeId, @RequestBody LeaveType leaveType) {

        LeaveType updated = leaveTypeService.updateLeaveType(leaveTypeId, leaveType);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete/{leaveTypeId}")
    public ResponseEntity<String> deleteLeaveType(@PathVariable Long leaveTypeId) {
        leaveTypeService.deleteLeaveType(leaveTypeId);
        return ResponseEntity.ok("Leave remove successfully");
    }
}

