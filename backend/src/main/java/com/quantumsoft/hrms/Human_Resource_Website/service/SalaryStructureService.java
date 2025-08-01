package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.SalaryStructure;
import java.util.List;
import java.util.UUID;

public interface SalaryStructureService{

    SalaryStructure createSalaryStructure(Long employeeId, SalaryStructure newStructure);

    List<SalaryStructure> getAll();

    SalaryStructure getCurrentStructureForEmployee(Long employeeId);

    List<SalaryStructure> getStructuresForEmployee(Long employeeId);

   void deleteStructure(Long id);


}
