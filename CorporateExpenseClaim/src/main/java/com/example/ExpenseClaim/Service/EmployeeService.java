package com.example.ExpenseClaim.Service;

import com.example.ExpenseClaim.Dto.EmployeeResponseDto;
import com.example.ExpenseClaim.Entity.Employee;

import java.util.List;

public interface EmployeeService {

     EmployeeResponseDto createEmployee(Employee employee);
     List<EmployeeResponseDto> getAll();
     EmployeeResponseDto getById(Integer id);
     EmployeeResponseDto updateEmployee(Integer id, EmployeeResponseDto employeeResponseDto);
     void deleteEmployee(Integer id);
}
