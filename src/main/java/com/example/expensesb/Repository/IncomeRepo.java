package com.example.expensesb.Repository;

import com.example.expensesb.Entity.Income;
import com.example.expensesb.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Long> {

    List<Income> findByUser(MyUser user);
}
