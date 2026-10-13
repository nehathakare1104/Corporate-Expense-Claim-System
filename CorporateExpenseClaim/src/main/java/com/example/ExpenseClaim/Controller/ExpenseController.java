package com.example.ExpenseClaim.Controller;

import com.example.ExpenseClaim.ApiResponse.ApiResponse;
import com.example.ExpenseClaim.Dto.*;
import com.example.ExpenseClaim.Entity.Expenses;
import com.example.ExpenseClaim.Service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ExpenseResponseDto submitExpense(@RequestBody @Valid ExpenseRequestDto expense){
        return expenseService.submitExpense(expense);

    }

    @GetMapping("/my")
    public List<ExpenseResponseDto> getMyExpenses(){
        return expenseService.getMyExpenses();
    }

    @GetMapping("/pending")
    public List<ExpenseResponseDto> pendingExpenses(){
        return expenseService.pendingExpenses();
    }


    @PutMapping("/{id}/approve")
    public ExpenseResponseDto approveExpense(@PathVariable Long id,
                                             @Valid @RequestBody CommentResponseDto request){
        return expenseService.approveExpense(id,request);
    }

    @PutMapping("/{id}/reject")
    public ExpenseResponseDto rejectExpense(@PathVariable Long id,
                                            @Valid @RequestBody CommentResponseDto request){
        return expenseService.rejectExpense(id,request);
    }

    @GetMapping("/approve")
    public List<ExpenseResponseDto> approveExpensesList(){
        return expenseService.approveExpenseList();
    }

    @PutMapping("/{id}/pay")
    public ExpenseResponseDto payClaim(@PathVariable Long id,
                                       @Valid @RequestBody CommentResponseDto request){
        return expenseService.PayClaim(id, request);
    }

    @GetMapping("/filter")
    public List<ExpenseResponseDto> filterExpense(
            @RequestParam(required = false) Expenses.Status status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate
            ){
        return expenseService.filterExpense(status,category,minAmount,maxAmount,fromDate,toDate);
    }

    @GetMapping("/category_total")
    public List<categoryResponseDto> getCategoryTotal(){
        return expenseService.getCategoryTotal();
    }

    @GetMapping("/{id}")
    public ExpenseResponseDto getById(@PathVariable Long id){
        return expenseService.getById(id);
    }
}