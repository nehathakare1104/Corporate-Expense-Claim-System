package com.example.ExpenseClaim.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "comments")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer commentId;
    @NotBlank(message = "Comment is required")
    private String comment;
    @PastOrPresent(message = "Date not the futureDate")
    private LocalDate createAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name ="expense_id")
    private Expenses expense;

}
