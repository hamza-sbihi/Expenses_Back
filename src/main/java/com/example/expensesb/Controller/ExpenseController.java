package com.example.expensesb.Controller;


import com.example.expensesb.DTO.ExpenseReq;
import com.example.expensesb.DTO.ExpenseRes;
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
    public ResponseEntity<List<ExpenseRes>> getAllExpenses(){
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @PostMapping
    public ResponseEntity<ExpenseRes> addExpense(@RequestBody ExpenseReq expense){
        return ResponseEntity.ok(expenseService.create(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseRes> updateExpense(@RequestBody ExpenseReq expense,@PathVariable Long id){
        ExpenseRes savedExpense = expenseService.update(expense,id);
        return ResponseEntity.ok(savedExpense);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id){
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalExpenses(){
        return ResponseEntity.ok(expenseService.getTotal());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ExpenseRes>> getExpensesByCategory(@PathVariable Long id){
        return ResponseEntity.ok(expenseService.getExpenseByCategory(id));
    }

    @GetMapping("/total/category/{id}")
    public ResponseEntity<Double> getTotalExpensesByCategory(@PathVariable Long id){
        return ResponseEntity.ok(expenseService.getTotalByCategory(id));
    }

    @GetMapping("/date")
    public ResponseEntity<List<ExpenseRes>> getExpensesByMonth(@RequestParam int year, @RequestParam int month){
        return ResponseEntity.ok(expenseService.getExpensesByMonth(month,year));
    }

    @GetMapping("/total/date")
    public ResponseEntity<Double> getTotalExpensesByMonth(@RequestParam int month, @RequestParam int year){
        return ResponseEntity.ok(expenseService.getTotalExpensesByMonth(month,year));
    }



}
