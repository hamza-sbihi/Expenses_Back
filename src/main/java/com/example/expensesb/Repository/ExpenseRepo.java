package com.example.expensesb.Repository;

import com.example.expensesb.DTO.Stats.DonutChartRes;
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
    List<Expense> findByCategoryAndUser(@Param("user")MyUser user,
                                        @Param("category") Category category);

    @Query("SELECT e FROM Expense e WHERE e.category = :category")
    List<Expense> findByCategory(@Param("category")Category category);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date >= :start AND e.date < :end")
    List<Expense> findbyUserAndMonth(@Param("user")MyUser user,
                                     @Param("start") LocalDate start,
                                     @Param("end") LocalDate end);

    @Query("SELECT NEW com.example.expensesb.DTO.Stats.DonutChartRes(e.category.name,SUM(e.cost)) " +
            "FROM Expense e " +
            "WHERE e.user = :user AND e.date >= :start " +
            "AND e.date < :end " +
            "GROUP BY e.category.name")
    List<DonutChartRes> findByDonutChartData(@Param("user") MyUser user,
                                             @Param("start") LocalDate start,
                                             @Param("end") LocalDate end);

    @Query(value = "SELECT DATE_TRUNC(:period,e.date) AS period, SUM(e.cost) AS total " +
                     "FROM expense e WHERE e.user_id = :user AND e.date >= :start AND e.date < :end " +
                     "GROUP BY DATE_TRUNC(:period,e.date) ORDER BY period", nativeQuery = true)
    List<Object[]> findByHistoData(@Param("start") LocalDate start,
                                   @Param("end") LocalDate end,
                                   @Param("period") String period,
                                   @Param("user") Long user);
}
