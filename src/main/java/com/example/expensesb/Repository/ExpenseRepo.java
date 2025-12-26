package com.example.expensesb.Repository;

import com.example.expensesb.Entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {

}
