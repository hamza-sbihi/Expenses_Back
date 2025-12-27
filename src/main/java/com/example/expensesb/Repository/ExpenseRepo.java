package com.example.expensesb.Repository;

import com.example.expensesb.Entity.Expense;
import com.example.expensesb.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {

    List<Expense> findByUser(MyUser user);

}
