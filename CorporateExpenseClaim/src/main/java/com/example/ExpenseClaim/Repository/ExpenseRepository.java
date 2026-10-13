package com.example.ExpenseClaim.Repository;

import com.example.ExpenseClaim.Dto.ExpenseResponseDto;
import com.example.ExpenseClaim.Entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expenses, Long> {

    List<Expenses> findByEmployeeId(Integer EmployeeId);

    @Query(value = """
            SELECT e
            FROM Expenses e
            JOIN e.employee emp
            WHERE emp.department = :department
            AND e.status = 'PENDING'
            """)
    List<Expenses> findPendingExpensesByDepartment(@Param("department") String department);

    List<Expenses> findByStatus(Expenses.Status status);

    @Query("""
       SELECT e
       FROM Expenses e
       WHERE (:status IS NULL OR e.status = :status)
       AND   (:category IS NULL OR e.category = :category)
       AND   (:minAmount IS NULL OR e.amount >= :minAmount)
       AND   (:maxAmount IS NULL OR e.amount <= :maxAmount)
       AND   (:fromDate IS NULL OR e.date >= :fromDate)
       AND   (:toDate IS NULL OR e.date <= :toDate)
""")
    List<Expenses> filterExpenses(
            @Param("status") Expenses.Status status,
            @Param("category") String category,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query(value = """
          SELECT category, SUM(amount)
          FROM expenses 
          WHERE MONTH(date) = MONTH(CURRENT_DATE())
          AND YEAR(date)  = YEAR(CURRENT_DATE())
          GROUP BY category
          """, nativeQuery = true)
    List<Object[]> getCategoryTotal();
}
