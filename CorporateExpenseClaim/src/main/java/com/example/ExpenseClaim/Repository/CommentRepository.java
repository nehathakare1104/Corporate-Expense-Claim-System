package com.example.ExpenseClaim.Repository;

import com.example.ExpenseClaim.Entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comments , Integer> {

    List<Comments> findByExpenseId(Long expenseId);
}
