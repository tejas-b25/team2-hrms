package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import com.quantumsoft.hrms.Human_Resource_Website.service.AuditLogService;
import com.quantumsoft.hrms.Human_Resource_Website.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@Tag( name="User API's", description = "All operation related to user")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuditLogService auditLogService;


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok("User created successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String token = userService.login(username, password);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }


    @PostMapping("/forgotPwd")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        userService.sendOtp(request.get("email"));
        return ResponseEntity.ok("OTP sent to email.");
    }

    @PatchMapping("/resetPwd")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String otp,
                                           @RequestParam String newPassword) {
        userService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok("Password updated.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        // Optional: if you had token invalidation logic, you’d handle it here.
        // For now, we just log the logout event.
        auditLogService.logEvent(username, "LOGOUT");
        return ResponseEntity.ok("Logout successful.");
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }



    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Status status) {
        userService.updateStatus(id, status);
        return ResponseEntity.ok("Status updated.");
    }

    @PutMapping("/role_change/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> changeUserRole(@PathVariable("email") String email, @RequestParam Role role){
        userService.roleChangeByEmail(email, role);
        return  ResponseEntity.ok(Collections.singletonMap("message",
                "Role updated to " + role + " for user " + email));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

//    @GetMapping("/allEmp")
//    @PreAuthorize("hasAnyRole('ADMIN','HR')")
//    public ResponseEntity<List<User>> getAllUsersWithEmployee() {
//        List<User> users = userService.getUsersWithEmployee();
//        return ResponseEntity.ok(users);
//    }

}
