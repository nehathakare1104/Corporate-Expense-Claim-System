package com.example.ExpenseClaim.Service;

import com.example.ExpenseClaim.Dto.EmployeeResponseDto;
import com.example.ExpenseClaim.Entity.Employee;
import com.example.ExpenseClaim.Exception.ResourceNotFoundException;
import com.example.ExpenseClaim.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


//    create Employee
    @Override
    public EmployeeResponseDto createEmployee(Employee employee){
        if(employeeRepository.existsByEmail(employee.getEmail())){
            throw new RuntimeException("Email Already Exist");
        }
        String encodePassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodePassword);
        employeeRepository.save(employee);

        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartment(employee.getDepartment());
        dto.setReportingManageId(employee.getReportingManageId());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setRole(employee.getRole());
        return dto;
    }

//    Get All Employee
    @Override
    public List<EmployeeResponseDto> getAll() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponseDto> employeeResponseDto = new ArrayList<>();

        for(Employee employee:employees){
            EmployeeResponseDto employeeDto = new EmployeeResponseDto();
            employeeDto.setId(employee.getId());
            employeeDto.setName(employee.getName());
            employeeDto.setEmail(employee.getEmail());
            employeeDto.setDepartment(employee.getDepartment());
            employeeDto.setJoiningDate(employee.getJoiningDate());
            employeeDto.setPhone(employee.getPhone());
            employeeDto.setReportingManageId(employee.getReportingManageId());
            employeeDto.setRole(employee.getRole());

            employeeResponseDto.add(employeeDto);
        }
       return employeeResponseDto;
    }

//    Get Employee By Id
    public EmployeeResponseDto getById(Integer id){
       Employee employee = employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee Not Exists"));

        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartment(employee.getDepartment());
        dto.setReportingManageId(employee.getReportingManageId());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setRole(employee.getRole());
        return dto;
    }

//    Update Employee
    @Override
    public EmployeeResponseDto updateEmployee(Integer id, EmployeeResponseDto employeeResponseDto){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee Not Exists"));

        employee.setName(employeeResponseDto.getName());
        employee.setEmail(employeeResponseDto.getEmail());
        employee.setDepartment(employeeResponseDto.getDepartment());
        employee.setPhone(employeeResponseDto.getPhone());
        employee.setReportingManageId(employeeResponseDto.getReportingManageId());
        employee.setRole(employeeResponseDto.getRole());

        Employee updateEmployee = employeeRepository.save(employee);

        EmployeeResponseDto response = new EmployeeResponseDto();
        response.setId(updateEmployee.getId());
        response.setName(updateEmployee.getName());
        response.setEmail(updateEmployee.getEmail());
        response.setJoiningDate(updateEmployee.getJoiningDate());
        response.setDepartment(updateEmployee.getDepartment());
        response.setPhone(updateEmployee.getPhone());
        response.setReportingManageId(updateEmployee.getReportingManageId());
        response.setRole(updateEmployee.getRole());

        return response;
    }

    public void deleteEmployee(Integer id){
      if(!employeeRepository.existsById(id)){
           throw  new ResourceNotFoundException("Employee Not Exists");
      }

      employeeRepository.deleteById(id);
    }
}
