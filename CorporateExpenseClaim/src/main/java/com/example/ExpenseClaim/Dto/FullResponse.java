package com.example.ExpenseClaim.Dto;

import com.example.ExpenseClaim.Entity.Comments;
import com.example.ExpenseClaim.Entity.Expenses;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FullResponse {
    private Long id;
    private EmployeeResponseDto employee;
    private String category;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private LocalDateTime approvedAt;
    private LocalDateTime PaidAt;
    private Expenses.Status status;

   private List<Comments> comments;
}
