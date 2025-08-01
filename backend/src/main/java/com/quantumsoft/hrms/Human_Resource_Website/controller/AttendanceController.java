package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Attendance;
import com.quantumsoft.hrms.Human_Resource_Website.entity.ClockInRequest;
import com.quantumsoft.hrms.Human_Resource_Website.entity.ClockOutRequest;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.serviceImpl.AttendanceServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceServiceImpl attendanceService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    @PostMapping("/clock-in")
    public ResponseEntity<?> clockIn(@RequestBody ClockInRequest request) {
        try {
            attendanceService.clockIn(request);
            return ResponseEntity.ok("Clock-in successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    @PutMapping("/clock-out")
    public ResponseEntity<String> clockOut() {
        try {
            attendanceService.clockOut();
            return ResponseEntity.ok("Clock-out successful");
        } catch (IllegalArgumentException | ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + ex.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @GetMapping("/report/csv")
    public void generateCsvReport(HttpServletResponse response,
                                  @RequestParam(required = false) Long empId,
                                  @RequestParam(required = false) String fromDate,
                                  @RequestParam(required = false) String toDate) throws IOException {
        attendanceService.generateCsvReport(response, empId, fromDate, toDate);
    }
    @GetMapping("/report/pdf")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public void generatePdfReport(HttpServletResponse response,
                                  @RequestParam(required = false) Long empId,
                                  @RequestParam(required = false) String department,
                                  @RequestParam(required = false) String fromDate,
                                  @RequestParam(required = false) String toDate) throws IOException {
        attendanceService.generatePdfReport(response, empId, department, fromDate, toDate);
    }
    @GetMapping("/status/{empId}")
    public ResponseEntity<?> getMonthlyStatus(
            @PathVariable Long empId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") int month,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int year) {

        Map<String, Object> result = attendanceService.getMonthlyStatus(empId, month, year);
        return ResponseEntity.ok(result);
}
    @PostMapping("/regularize/request")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
    public ResponseEntity<String> requestRegularization(
//                                                        @RequestParam @NotNull UUID empId,
                                                        @RequestParam @NotNull String date,
                                                        @RequestParam String reason) {
        attendanceService.requestRegularization(date, reason);
        return ResponseEntity.ok("Regularization request submitted successfully.");
    }

    @PutMapping("/regularize/approve/{attendanceId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<String> approveRegularization(@PathVariable Long attendanceId) {
        attendanceService.approveRegularization(attendanceId);
        return ResponseEntity.ok("Regularization approved successfully.");
    }

    @PutMapping("/regularize/reject/{attendanceId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<String> rejectRegularization(@PathVariable Long attendanceId,
                                                       @RequestParam String rejectionReason) {
        attendanceService.rejectRegularization(attendanceId, rejectionReason);
        return ResponseEntity.ok("Regularization rejected.");
    }

    @GetMapping("/regularize/requests/all")
    @PreAuthorize("hasAnyRole('HR.'ADMIN')")
    public ResponseEntity<List<Attendance>> getAllRegularizationRequests(){
       List<Attendance> requests = attendanceService.getAllRegularizationRequests();
    return ResponseEntity.ok(requests);

    }

}
