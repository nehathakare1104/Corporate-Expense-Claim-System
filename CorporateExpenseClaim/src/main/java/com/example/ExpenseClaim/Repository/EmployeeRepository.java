package com.example.ExpenseClaim.Repository;

import com.example.ExpenseClaim.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);
}
