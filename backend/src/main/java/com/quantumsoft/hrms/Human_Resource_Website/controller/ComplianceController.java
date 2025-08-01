package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Compliance;
import com.quantumsoft.hrms.Human_Resource_Website.service.ComplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/compliances")
public class ComplianceController {
    @Autowired
    private ComplianceService complianceService;

    @PostMapping("/create")
    public Compliance addCompliance(@RequestBody Compliance compliance) {
        return complianceService.createCompliance(compliance);
    }
    @GetMapping("/all")
    public List<Compliance> getAllCompliances() {
        return complianceService.getAllCompliances();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Compliance> getComplianceById(@PathVariable Long id) {
        Compliance compliance = complianceService.getComplianceById(id);
        return ResponseEntity.ok(compliance);
    }

    @PutMapping("/update/{id}")
    public Compliance updateCompliance(@PathVariable Long id, @RequestBody Compliance compliance) {
        return complianceService.updateCompliance(id, compliance);
    }
    @DeleteMapping("/delete/{id}")
    public void deactivateCompliance(@PathVariable Long id) {
        complianceService.deactivateCompliance(id);
    }

}
