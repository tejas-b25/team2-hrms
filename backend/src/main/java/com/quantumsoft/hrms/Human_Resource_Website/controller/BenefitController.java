package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Benefit;
import com.quantumsoft.hrms.Human_Resource_Website.service.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/benefits")
public class BenefitController {

    @Autowired
    private BenefitService benefitService;

    @PreAuthorize("ADMIN/HR")
    @PostMapping("/create")
    public Benefit createBenefit(@RequestBody Benefit benefit) {
        return benefitService.createBenefit(benefit);
    }

    @PutMapping("/update/{id}")
    public Benefit updateBenefit(@PathVariable Long id, @RequestBody Benefit benefit) {
        return benefitService.updateBenefit(id, benefit);
    }

    @GetMapping("/all")
    public List<Benefit> getAllBenefits() {
        return benefitService.getAllBenefits();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        benefitService.deleteBenefit(id);
        return ResponseEntity.noContent().build();
    }

}
