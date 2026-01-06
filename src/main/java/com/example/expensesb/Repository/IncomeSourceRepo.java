package com.example.expensesb.Repository;

import com.example.expensesb.Entity.IncomeSource;
import com.example.expensesb.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeSourceRepo extends JpaRepository<IncomeSource, Long> {

    List<IncomeSource> findByUser(MyUser user);
}
