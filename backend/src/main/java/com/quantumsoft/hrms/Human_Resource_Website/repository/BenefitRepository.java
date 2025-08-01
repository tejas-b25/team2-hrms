package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
}
