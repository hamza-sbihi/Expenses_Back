package com.example.expensesb.Repository;

import com.example.expensesb.Entity.Category;
import com.example.expensesb.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long>{

    List<Category> findByUser(MyUser user);
}
