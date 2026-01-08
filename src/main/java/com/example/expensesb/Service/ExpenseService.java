package com.example.expensesb.Service;

import com.example.expensesb.DTO.ExpenseReq;
import com.example.expensesb.DTO.ExpenseRes;
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

import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<ExpenseRes> getAllExpenses(){

        MyUser user = getUserFromContext();

        List<Expense> expenses = expenseRepo.findByUser(user);

        List<ExpenseRes> expensesRes = new ArrayList<>();

        for (Expense expense : expenses) {
            ExpenseRes expenseRes = new ExpenseRes();
            createResFromDb(expenseRes, expense);
            expensesRes.add(expenseRes);
        }


        return  expensesRes;

    }
    public ExpenseRes create(ExpenseReq expense){
        MyUser user = getUserFromContext();

        Category category = categoryRepo.findById(expense.getCategoryId())
                .orElseThrow(()->new EntityNotFoundException("Category not found creating expense"));
        if(!Objects.equals(category.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while creating expense");
        }
        Expense newExpense = new Expense();
        newExpense.setUser(user);
        newExpense.setCategory(category);
        newExpense.setCost(expense.getCost());
        newExpense.setDate(expense.getDate());
        newExpense.setDescription(expense.getDescription());

        newExpense =  expenseRepo.save(newExpense);

        ExpenseRes expenseRes = new ExpenseRes();

        createResFromDb(expenseRes, newExpense);

        return expenseRes;
    }

    public ExpenseRes update(ExpenseReq expense, Long id) {
        Expense expenseD = expenseRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Expense not found with id: " + id));

        MyUser user = getUserFromContext();

        Category category = categoryRepo.findById(expense.getCategoryId())
                .orElseThrow(()->new EntityNotFoundException("Category not found while updating expense with id: " + id));


        if(!Objects.equals(expenseD.getUser().getId(), user.getId()) || !Objects.equals(category.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        expenseD.setCategory(category);
        expenseD.setDescription(expense.getDescription());
        expenseD.setDate(expense.getDate());
        expenseD.setCost(expense.getCost());
        expenseD = expenseRepo.save(expenseD);

        ExpenseRes expenseRes = new ExpenseRes();

        createResFromDb(expenseRes, expenseD);

        return expenseRes;
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

    public Double getTotal() {
        List<ExpenseRes> expenses = getAllExpenses();
        Double total = 0.0;
        for(ExpenseRes expense : expenses){
            total += expense.getCost();
        }
        return total;
    }

    public Double getTotalByCategory(Long id) {
        MyUser user = getUserFromContext();

        //fetching the category from DB
        Category category = categoryRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not found while updating expense with id: " + id));

        //checking if the category is actually by the user
        if(!Objects.equals(category.getUser().getId(),user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Expense> expenses = expenseRepo.findByCategoryAndUser(user,category);

        Double total = 0.0;
        for(Expense expense : expenses){
            total += expense.getCost();
        }
        return total;
    }

    public List<ExpenseRes> getExpenseByCategory(Long id) {
        MyUser user = getUserFromContext();

        //fetching the category from DB
        Category category = categoryRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not found while updating expense with id: " + id));

        //checking if the category is actually by the user
        if(!Objects.equals(category.getUser().getId(),user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Expense> expenses = expenseRepo.findByCategoryAndUser(user,category);

        List<ExpenseRes> expenseRes = new ArrayList<>();

        for(Expense expense : expenses){
            ExpenseRes expenseRes1 = new ExpenseRes();
            createResFromDb(expenseRes1, expense);
            expenseRes.add(expenseRes1);
        }

        return expenseRes;
    }

    public List<ExpenseRes> getExpensesByMonth(int month, int year) {
        MyUser user = getUserFromContext();
        LocalDate start =  LocalDate.of(year, month, 1);

        LocalDate end = start.plusMonths(1);

        List<Expense> expenses = expenseRepo.findbyUserAndMonth(user,start,end);

        List<ExpenseRes> expenseRes = new ArrayList<>();

        for(Expense expense : expenses){
            ExpenseRes expenseRes1 = new ExpenseRes();
            createResFromDb(expenseRes1, expense);
            expenseRes.add(expenseRes1);
        }

        return expenseRes;
    }

    public Double getTotalExpensesByMonth(int month, int year) {
        MyUser user = getUserFromContext();
        LocalDate start =  LocalDate.of(year, month, 1);

        LocalDate end = start.plusMonths(1);

        List<Expense> expenses = expenseRepo.findbyUserAndMonth(user,start,end);
        Double total = 0.0;
        for(Expense expense : expenses){
            total += expense.getCost();
        }
        return total;
    }

    public MyUser getUserFromContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return myUserRepo.findByUsername(username).orElseThrow(()->new EntityNotFoundException("User not found in Expense Service"));
    }

    public void createResFromDb(ExpenseRes expenseRes,Expense expense){

        expenseRes.setId(expense.getId());
        expenseRes.setCategoryId(expense.getCategory().getId());
        expenseRes.setCategoryName(expense.getCategory().getName());
        expenseRes.setCost(expense.getCost());
        expenseRes.setDate(expense.getDate());
        expenseRes.setDescription(expense.getDescription());


    }



}
