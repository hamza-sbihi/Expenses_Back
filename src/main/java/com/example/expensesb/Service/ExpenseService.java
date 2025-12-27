package com.example.expensesb.Service;

import com.example.expensesb.Entity.Category;
import com.example.expensesb.Entity.Expense;
import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Repository.CategoryRepo;
import com.example.expensesb.Repository.ExpenseRepo;
import com.example.expensesb.Repository.MyUserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final MyUserRepo myUserRepo;
    private final CategoryRepo categoryRepo;

    public ExpenseService(ExpenseRepo expenseRepo, MyUserRepo myUserRepo, CategoryRepo categoryRepo) {
        this.expenseRepo = expenseRepo;
        this.myUserRepo = myUserRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Expense> getAllExpenses(){

        MyUser user = getUserFromContext();

        return expenseRepo.findByUser(user);

    }
    public Expense create(Expense expense){
        MyUser user = getUserFromContext();

        Category category = categoryRepo.findById(expense.getCategory().getId())
                .orElseThrow(()->new EntityNotFoundException("Category not found creating expense"));
        if(!Objects.equals(category.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        expense.setUser(user);
        expense.setCategory(category);

        return expenseRepo.save(expense);
    }

    public Expense update(Expense expense, Long id) {
        Expense expenseD = expenseRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Expense not found with id: " + id));

        MyUser user = getUserFromContext();

        Category category = categoryRepo.findById(expense.getCategory().getId())
                .orElseThrow(()->new EntityNotFoundException("Category not found while updating expense with id: " + id));


        if(!Objects.equals(expenseD.getUser().getId(), user.getId()) || !Objects.equals(category.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        expenseD.setCategory(category);
        expenseD.setDescription(expense.getDescription());
        expenseD.setDate(expense.getDate());
        expenseD.setCost(expense.getCost());

        return expenseRepo.save(expenseD);
    }

    public void delete(Long id) {

        Expense expenseD = expenseRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Expense not found with id: " + id));
        MyUser user = getUserFromContext();

        if(!Objects.equals(expenseD.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        expenseRepo.delete(expenseD);
    }

    public MyUser getUserFromContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return myUserRepo.findByUsername(username).orElseThrow(()->new EntityNotFoundException("User not found in Expense Service"));
    }
}
