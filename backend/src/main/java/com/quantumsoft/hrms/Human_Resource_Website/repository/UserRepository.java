package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);

    boolean existsByRole(Role role);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

//    @Query("SELECT u.email FROM User u WHERE u.role = :role")
//    List<String> findEmailsByRole(Role role);

    //    @Query("SELECT u FROM User u LEFT JOIN FETCH u.employee WHERE u.role = :role")
    //    List<User> findByRoleWithEmployee(@Param("role") Role role);
    //@Query("SELECT u FROM User u LEFT JOIN FETCH u.employee")
    //List<User> findAllWithEmployee();
}
