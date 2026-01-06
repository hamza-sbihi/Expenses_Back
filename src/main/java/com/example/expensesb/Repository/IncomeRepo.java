package com.example.expensesb.Repository;

import com.example.expensesb.Entity.Income;
import com.example.expensesb.Entity.IncomeSource;
import com.example.expensesb.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Long> {

    List<Income> findByUser(MyUser user);

    @Query("SELECT i FROM Income i WHERE i.user = :user AND i.incomeSource = :source")
    List<Income> findByUserAndSource(@Param("user") MyUser user, @Param("source") IncomeSource source);

    @Query("SELECT i FROM Income i WHERE i.user = :user AND i.date >= :start AND i.date < :end")
    List<Income> findByUserAndMonth(@Param("user") MyUser user, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
