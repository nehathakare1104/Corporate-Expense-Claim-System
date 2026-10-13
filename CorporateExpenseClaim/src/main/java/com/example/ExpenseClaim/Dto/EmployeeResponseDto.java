package com.example.ExpenseClaim.Dto;

import com.example.ExpenseClaim.Entity.Employee.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeResponseDto {

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String reportingManageId;
    private LocalDate joiningDate;
    private Role role;


}
