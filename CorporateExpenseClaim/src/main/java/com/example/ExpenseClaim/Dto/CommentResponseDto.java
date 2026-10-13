package com.example.ExpenseClaim.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    @NotBlank(message = "Comment is required")
    private String comment;
}
