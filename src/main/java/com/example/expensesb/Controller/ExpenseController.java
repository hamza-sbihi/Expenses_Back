package com.example.expensesb.Controller;


import com.example.expensesb.Entity.Expense;
import com.example.expensesb.Service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController( ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(){
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense){
        return ResponseEntity.ok(expenseService.create(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@RequestBody Expense expense,@PathVariable Long id){
        Expense savedExpense = expenseService.update(expense,id);
        return ResponseEntity.ok(savedExpense);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id){
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
