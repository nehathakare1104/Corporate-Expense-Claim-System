package com.example.ExpenseClaim.Service;

import com.example.ExpenseClaim.Dto.CommentResponseDto;
import com.example.ExpenseClaim.Dto.ExpenseRequestDto;
import com.example.ExpenseClaim.Dto.ExpenseResponseDto;
import com.example.ExpenseClaim.Dto.categoryResponseDto;
import com.example.ExpenseClaim.Entity.Comments;
import com.example.ExpenseClaim.Entity.Employee;
import com.example.ExpenseClaim.Entity.Expenses;
import com.example.ExpenseClaim.Exception.ResourceNotFoundException;
import com.example.ExpenseClaim.Repository.CommentRepository;
import com.example.ExpenseClaim.Repository.EmployeeRepository;
import com.example.ExpenseClaim.Repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.xml.stream.events.Comment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EmployeeRepository empRepository;

    @Autowired
    private CommentRepository commentRepository;

    public ExpenseResponseDto submitExpense(ExpenseRequestDto expense) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Employee employee = empRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Employee not exists"));

        Expenses expenses = new Expenses();
        expenses.setEmployee(employee);
        expenses.setCategory(expense.getCategory());
        expenses.setAmount(expense.getAmount());
        expenses.setDate(expense.getDate());
        expenses.setDescription(expense.getDescription());
        expenses.setApprovedAt(null);
        expenses.setPaidAt(null);
        expenses.setStatus(Expenses.Status.PENDING);
        expenseRepository.save(expenses);

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setId(expenses.getId());
        response.setEmployeeId(employee.getId());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setDate(expense.getDate());
        response.setApprovedAt(expenses.getApprovedAt());
        response.setPaidAt(expenses.getPaidAt());
        response.setDescription(expense.getDescription());
        response.setStatus(expenses.getStatus());
        return response;
    }

    //    Get My Expenses
    public List<ExpenseResponseDto> getMyExpenses() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Employee employee = empRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        List<Expenses> expenses = expenseRepository.findByEmployeeId(employee.getId());


        List<ExpenseResponseDto> expenseList = new ArrayList<>();

        for (Expenses expense : expenses) {
            ExpenseResponseDto response = new ExpenseResponseDto();

            response.setId(expense.getId());
            response.setCategory(expense.getCategory());
            response.setAmount(expense.getAmount());
            response.setDescription(expense.getDescription());
            response.setEmployeeId(employee.getId());
            response.setStatus(expense.getStatus());
            response.setDate(expense.getDate());
            response.setApprovedAt(expense.getApprovedAt());
            response.setPaidAt(expense.getPaidAt());

            List<Comments> comments = commentRepository.findByExpenseId(expense.getId());
            response.setComment(comments);

            expenseList.add(response);
        }
        return expenseList;
    }


    //    Get Pending List
    public List<ExpenseResponseDto> pendingExpenses() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Employee manager = empRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Manager Not Exist"));

        String department = manager.getDepartment();

        System.out.println("Manager: " + manager.getName());
        System.out.println("Manager Department: " + manager.getDepartment());

        List<Expenses> expenses = expenseRepository.findPendingExpensesByDepartment(department);

        List<ExpenseResponseDto> expenseList = new ArrayList<>();
        for (Expenses expense : expenses) {
            ExpenseResponseDto response = new ExpenseResponseDto();
            response.setId(expense.getId());
            response.setDate(expense.getDate());
            response.setEmployeeId(expense.getEmployee().getId());
            response.setAmount(expense.getAmount());
            response.setCategory(expense.getCategory());
            response.setDescription(expense.getDescription());
            response.setStatus(expense.getStatus());
            response.setPaidAt(expense.getPaidAt());
            response.setApprovedAt(expense.getApprovedAt());

            expenseList.add(response);
        }
        return expenseList;
    }

    //   Approve the Expense Claim
    public ExpenseResponseDto approveExpense(Long id, CommentResponseDto request) {
        Expenses expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not Exists"));

        if (expense.getStatus() != Expenses.Status.PENDING) {
            throw new RuntimeException("Only pending expense can be approve");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Employee manager = empRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Manager not exists"));

        String managerDept = manager.getDepartment();

        System.out.println("ManagerDept is : " + managerDept);

        String employeeDept = expense.getEmployee().getDepartment();

        System.out.println("employeeDept is : " + employeeDept);

        if (!managerDept.equals(employeeDept)) {
            throw new RuntimeException("You can only approve your department expenses");
        }

        expense.setStatus(Expenses.Status.APPROVED);
        expense.setApprovedAt(LocalDateTime.now());

        expenseRepository.save(expense);

        //   comment save
        Comments comment = new Comments() ;
        comment.setComment(request.getComment());
        comment.setExpense(expense);
        comment.setCreateAt(LocalDate.now());
        commentRepository.save(comment);

        List<Comments> comments = commentRepository.findByExpenseId(expense.getId());

        ExpenseResponseDto response = new ExpenseResponseDto();

        response.setId(expense.getId());
        response.setEmployeeId(expense.getEmployee().getId());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setDate(expense.getDate());
        response.setApprovedAt(expense.getApprovedAt());
        response.setPaidAt(expense.getPaidAt());
        response.setStatus(expense.getStatus());
        response.setComment(comments);

        return response;
    }

   // Reject the Expense Claim
    public ExpenseResponseDto rejectExpense(Long id, CommentResponseDto request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Employee manager = empRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Manager not exists"));

        Expenses expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not Exists"));

        if (expense.getStatus() != Expenses.Status.PENDING) {
            throw new RuntimeException("Only pending expense can be reject");
        }

        String managerDept = manager.getDepartment();

        System.out.println("ManagerDept is : " + managerDept);

        String employeeDept = expense.getEmployee().getDepartment();

        System.out.println("employeeDept is : " + employeeDept);

        if (!managerDept.equals(employeeDept)) {
            throw new RuntimeException("You can only reject your department expenses");
        }

        expense.setStatus(Expenses.Status.REJECTED);

        expenseRepository.save(expense);

        //   comment save
        Comments comment = new Comments() ;
        comment.setComment(request.getComment());
        comment.setExpense(expense);
        comment.setCreateAt(LocalDate.now());
        commentRepository.save(comment);

        List<Comments> comments = commentRepository.findByExpenseId(expense.getId());

        ExpenseResponseDto response = new ExpenseResponseDto();

        response.setId(expense.getId());
        response.setEmployeeId(expense.getEmployee().getId());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setDate(expense.getDate());
        response.setApprovedAt(expense.getApprovedAt());
        response.setPaidAt(expense.getPaidAt());
        response.setStatus(expense.getStatus());
        response.setComment(comments);

        return response;
    }

   public List<ExpenseResponseDto> approveExpenseList(){
        List<Expenses> expenses = expenseRepository.findByStatus(Expenses.Status.APPROVED);

        List<ExpenseResponseDto> expenseList = new ArrayList<>();

        for(Expenses expense : expenses){
            ExpenseResponseDto response = new ExpenseResponseDto();
            response.setId(expense.getId());
            response.setEmployeeId(expense.getEmployee().getId());
            response.setCategory(expense.getCategory());
            response.setDescription(expense.getDescription());
            response.setAmount(expense.getAmount());
            response.setStatus(expense.getStatus());
            response.setPaidAt(expense.getPaidAt());
            response.setDate(expense.getDate());
            response.setApprovedAt(expense.getApprovedAt());

            expenseList.add(response);
        }
        return expenseList;
   }

//   Pay Expense Claim

    public ExpenseResponseDto PayClaim(Long id, CommentResponseDto request){
        Expenses expense = expenseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Expense not Exists"));

        if(expense.getStatus() != Expenses.Status.APPROVED){
            throw new RuntimeException("Only Approve Expense Can paid");
        }

        expense.setStatus(Expenses.Status.PAID);
        expense.setPaidAt(LocalDateTime.now());
        expenseRepository.save(expense);

        //   comment save
        Comments comment = new Comments() ;
        comment.setComment(request.getComment());
        comment.setExpense(expense);
        comment.setCreateAt(LocalDate.now());
        commentRepository.save(comment);

        List<Comments> comments = commentRepository.findByExpenseId(expense.getId());

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setId(expense.getId());
        response.setEmployeeId(expense.getEmployee().getId());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setStatus(expense.getStatus());
        response.setDate(expense.getDate());
        response.setApprovedAt(expense.getApprovedAt());
        response.setPaidAt(expense.getPaidAt());
        response.setComment(comments);
        return response;
    }

//    dynamic Filter
    public List<ExpenseResponseDto> filterExpense(
            Expenses.Status status,
            String category,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDate fromDate,
            LocalDate toDate
            ){
        List<Expenses> expenses = expenseRepository.filterExpenses(status,category,minAmount,maxAmount,fromDate,toDate);

        List<ExpenseResponseDto> expenseList = new ArrayList<>();

        for(Expenses expense: expenses){
            ExpenseResponseDto response = new ExpenseResponseDto();
            response.setId(expense.getId());
            response.setEmployeeId(expense.getEmployee().getId());
            response.setStatus(expense.getStatus());
            response.setCategory(expense.getCategory());
            response.setDescription(expense.getDescription());
            response.setDate(expense.getDate());
            response.setAmount(expense.getAmount());
            response.setApprovedAt(expense.getApprovedAt());
            response.setPaidAt(expense.getPaidAt());
            expenseList.add(response);
        }
        return expenseList;
    }

//    List of total amount current Month Expenses based on category
    public List<categoryResponseDto> getCategoryTotal(){
        List<Object[]> result = expenseRepository.getCategoryTotal();
        List<categoryResponseDto> responseList = new ArrayList<>();

        for(Object[] row : result){
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];

            categoryResponseDto response = new categoryResponseDto(category,total);

            responseList.add(response);
        }
        return responseList;
    }

//    expense getById
    public ExpenseResponseDto getById(Long id) {
        Expenses expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not exist"));

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setId(expense.getId());
        response.setEmployeeId(expense.getEmployee().getId());
        response.setStatus(expense.getStatus());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setDate(expense.getDate());
        response.setAmount(expense.getAmount());
        response.setApprovedAt(expense.getApprovedAt());
        response.setPaidAt(expense.getPaidAt());

        List<Comments> comments = commentRepository.findByExpenseId(expense.getId());
        response.setComment(comments);
        return response;
    }
}
