package com.example.ExpenseClaim.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class categoryResponseDto {
    private String category;
    private BigDecimal total;
}
