package com.example.ExpenseClaim.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
import java.util.List;

@Table(name = "expenses")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY )
    @JoinColumn(name = "employee_id")
    @NotNull(message = "Employee id is required")
    private Employee employee;
    @NotBlank(message= "Category is required")
    private String category;
    @NotNull(message = "Amount is necessary")
    @DecimalMin(value= "0.01")
    private BigDecimal amount;
    @NotNull(message = "date is required")
    @PastOrPresent(message = "date cannot be in the future")
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private Status status;
    @NotBlank(message = "Description in required")
    private String description;
    @PastOrPresent(message = "Future date is not allowed")
    private LocalDateTime approvedAt;
    @PastOrPresent
    private LocalDateTime PaidAt;

    @OneToMany(mappedBy = "expense")
    private List<Comments> comments;



    public enum Status{
        PENDING,
        APPROVED,
        REJECTED,
        PAID
    }

}
