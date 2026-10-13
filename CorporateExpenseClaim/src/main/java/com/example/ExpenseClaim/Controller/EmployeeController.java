package com.example.ExpenseClaim.Controller;

import com.example.ExpenseClaim.ApiResponse.ApiResponse;
import com.example.ExpenseClaim.Dto.EmployeeResponseDto;
import com.example.ExpenseClaim.Entity.Employee;
import com.example.ExpenseClaim.Service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @PostMapping
    public ResponseEntity<ApiResponse> createEmployee(
            @RequestBody @Valid Employee employee) {

        EmployeeResponseDto employeeReturn =
                employeeService.createEmployee(employee);

        ApiResponse response = ApiResponse.builder()
                .message("Create Employee Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .data(employeeReturn)
                .errors(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll (){
        List<EmployeeResponseDto> employee = employeeService.getAll();
        ApiResponse response = ApiResponse.builder()
                .message("All Employee Fetched Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .data(employee)
                .errors(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable @Positive(message = "ID must be greater than 0"

    ) Integer id){
        EmployeeResponseDto employee = employeeService.getById(id);
        ApiResponse response = ApiResponse.builder()
                .message("Employee Fetched Successful")
                .success(true)
                .status(HttpStatus.OK)
                .data(employee)
                .errors(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeResponseDto employeeResponseDto){
        EmployeeResponseDto employee = employeeService.updateEmployee(id,employeeResponseDto );
        ApiResponse response = ApiResponse.builder()
                .message("Employee Update Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .data(employee)
                .errors(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Integer id){
        employeeService.deleteEmployee(id);
        ApiResponse response = ApiResponse.builder()
                .message("Employee delete Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);

    }


}
