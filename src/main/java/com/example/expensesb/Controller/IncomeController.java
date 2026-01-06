package com.example.expensesb.Controller;

import com.example.expensesb.Entity.Income;
import com.example.expensesb.Service.IncomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public ResponseEntity<List<Income>> getIncome() {
        return ResponseEntity.ok(incomeService.getAllIncomes());
    }

    @PostMapping
    public ResponseEntity<Income> createIncome(@RequestBody Income income) {
        return ResponseEntity.ok(incomeService.createIncome(income));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Income>  updateIncome(@PathVariable Long id, @RequestBody Income income) {
        return ResponseEntity.ok(incomeService.updateIncome(id,income));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
