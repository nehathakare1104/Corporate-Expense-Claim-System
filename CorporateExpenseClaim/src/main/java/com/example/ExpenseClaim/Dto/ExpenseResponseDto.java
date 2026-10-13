package com.example.ExpenseClaim.Dto;

import com.example.ExpenseClaim.Entity.Comments;
import com.example.ExpenseClaim.Entity.Expenses.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpenseResponseDto {
    private Long id;
    private Integer employeeId;
    private String category;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private LocalDateTime approvedAt;
    private LocalDateTime PaidAt;
    private Status status;

    private List<Comments> comment;
}
