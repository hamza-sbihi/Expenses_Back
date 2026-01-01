package com.example.expensesb.Repository;

import com.example.expensesb.Entity.Category;
import com.example.expensesb.Entity.Expense;
import com.example.expensesb.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {

    List<Expense> findByUser(MyUser user);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.category = :category" )
    List<Expense> findByCategoryAndUser(@Param("user")MyUser user,@Param("category") Category category);

    @Query("SELECT e FROM Expense e WHERE e.category = :category")
    List<Expense> findByCategory(@Param("category")Category category);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date >= :start AND e.date < :end")
    List<Expense> findbyUserAndMonth(@Param("user")MyUser user,@Param("start") LocalDate start,@Param("end") LocalDate end);
}
