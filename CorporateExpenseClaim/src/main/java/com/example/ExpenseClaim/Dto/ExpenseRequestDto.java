package com.example.ExpenseClaim.Dto;



import com.example.ExpenseClaim.Entity.Expenses.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequestDto {

    @NotBlank(message= "Category is required")
    private String category;
    @NotNull(message = "Amount is necessary")
    @DecimalMin(value= "0.01")
    private BigDecimal amount;
    @NotNull(message = "date is required")
    @PastOrPresent(message = "date cannot be in the future")
    private LocalDate date;
    private String description;
}
