package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Department;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.exception.FileStorageException;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.*;
import com.quantumsoft.hrms.Human_Resource_Website.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BankDetailRepository bankDetailRepository;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final String uploadDir = "/uploads/employees/";

    @Override
    public Employee createEmployee(Employee employee, MultipartFile photo, String email) {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        employee.setCreatedBy(currentUsername);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Email ID: " + email));
        employee.setEmail(email);
        employee.setUser(user);


//        if (employee.getManager() != null && employee.getManager().getUserID() != null) {
//            Long managerUserId = employee.getManager().getUserID();
//
//            User managerUser = userRepository.findById(managerUserId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Manager User not found with ID: " + managerUserId));
//
//            if (managerUser.getRole() != Role.MANAGER) {
//                throw new ResourceNotFoundException("Assigned Manager user does not have a MANAGER role.");
//            }
//
//            employee.setManager(managerUser);
//        } else {
//            employee.setManager(null);
//        }


        if (employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null) {
            Long deptId=employee.getDepartment().getDepartmentId();
            Department department = departmentRepository.findById(deptId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + employee.getDepartment().getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            employee.setPhoto(filePath.toString());
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }

        employee.setJoiningDate(LocalDate.now());
        employee.setProbationEndDate(LocalDate.now().plusMonths(3));
        SecurityContextHolder.getContext().getAuthentication();

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long empId, Employee employee, MultipartFile photo) {
        Employee existingEmployee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + empId));

        if (employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null) {
            Long deptId = employee.getDepartment().getDepartmentId();
            Department dept = departmentRepository.findById(deptId)
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + deptId));
            existingEmployee.setDepartment(dept);
        }

        if (employee.getContactNumber() != null) {
            existingEmployee.setContactNumber(employee.getContactNumber());
        }
        if (employee.getFirstName() != null) {
            existingEmployee.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null) {
            existingEmployee.setLastName(employee.getLastName());
        }
        if (employee.getAddress() != null) {
            existingEmployee.setAddress(employee.getAddress());
        }
        if (employee.getUpdatedAt() != null) {
            existingEmployee.setUpdatedAt(employee.getUpdatedAt());
        }
        if (employee.getCertifications() != null) {
            existingEmployee.setCertifications(employee.getCertifications());
        }
        if (employee.getContactNumber() != null) {
            existingEmployee.setContactNumber(employee.getContactNumber());
        }
        if (employee.getDob() != null) {
            existingEmployee.setDob(employee.getDob());
        }
        if (employee.getDesignation() != null) {
            existingEmployee.setDesignation(employee.getDesignation());
        }
        if (employee.getDepartment() != null) {
            existingEmployee.setDepartment(employee.getDepartment());
        }
            existingEmployee.setResignationDate(employee.getResignationDate());

        if (employee.getExperience() != null) {
            existingEmployee.setExperience(employee.getExperience());
        }
//        if (employee.getEmployeeCode() != null) {
//            existingEmployee.setEmployeeCode(employee.getEmployeeCode());
//        }
        if (employee.getEducation() != null) {
            existingEmployee.setEducation(employee.getEducation());
        }
        if (employee.getEmail() != null) {
            existingEmployee.setEmail(employee.getEmail());
        }
        if (employee.getEmergencyContact() != null) {
            existingEmployee.setCertifications(employee.getEmergencyContact());
        }
        if (employee.getEmployeeCode() != null) {
            existingEmployee.setEmployeeCode(employee.getEmployeeCode());
        }
        if (employee.getGender() != null) {
            existingEmployee.setGender(employee.getGender());
        }
        if (employee.getJobType() != null) {
            existingEmployee.setJobType(employee.getJobType());
        }
        if (employee.getJoiningDate() != null) {
            existingEmployee.setJoiningDate(employee.getJoiningDate());
        }
        if (employee.getLocation() != null) {
            existingEmployee.setLocation(employee.getLocation());
        }
        if (employee.getProbationEndDate() != null) {
            existingEmployee.setProbationEndDate(employee.getProbationEndDate());
        }
        if (employee.getStatus() != null) {
            existingEmployee.setStatus(employee.getStatus());
        }
        if (employee.getUser() != null) {
            existingEmployee.setUser(employee.getUser());
        }

        if (photo != null && !photo.isEmpty()) {
            String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);

            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingEmployee.setPhoto(filePath.toString());

            } catch (IOException e) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
            }
        }

        return employeeRepository.save(existingEmployee);

    }

    @Override
    public Employee getEmployeeById(Long empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + empId));
    }

    @Override
    public void deleteEmplyee(Long empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + empId));
        employeeRepository.delete(employee);
    }

    @Override
    public List<Employee> getEmployeesByRoleManager(Role role) {
        return employeeRepository.findByUserRole(role);
    }

    @Override
    public List<Employee> findAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        System.out.println("Total Records Found: " + employees.size());
        return employees;
    }


}
