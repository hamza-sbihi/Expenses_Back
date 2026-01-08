package com.example.expensesb.Controller;

import com.example.expensesb.DTO.IncomeReq;
import com.example.expensesb.DTO.IncomeRes;
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
    public ResponseEntity<List<IncomeRes>> getIncome() {
        return ResponseEntity.ok(incomeService.getAllIncomes());
    }

    @PostMapping
    public ResponseEntity<IncomeRes> createIncome(@RequestBody IncomeReq income) {
        return ResponseEntity.ok(incomeService.createIncome(income));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeRes>  updateIncome(@PathVariable Long id, @RequestBody IncomeReq income) {
        return ResponseEntity.ok(incomeService.updateIncome(id,income));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalIncomes(){
        return ResponseEntity.ok(incomeService.getTotal());
    }

    @GetMapping("/source/{id}")
    public ResponseEntity<List<IncomeRes>> getIncomeBySource(@PathVariable Long id){
        return ResponseEntity.ok(incomeService.getIncomeBySource(id));
    }

    @GetMapping("/total/source/{id}")
    public ResponseEntity<Double> getTotalIncomesOfSource(@PathVariable Long id){
        return ResponseEntity.ok(incomeService.getTotalBySource(id));
    }

    @GetMapping("/date")
    public ResponseEntity<List<IncomeRes>> getIncomeByMonth(@RequestParam int year,@RequestParam int month){
        return ResponseEntity.ok(incomeService.getIncomeByMonth(year,month));
    }

    @GetMapping("/total/date")
    public ResponseEntity<Double> getTotalIncomesByMonth(@RequestParam int year,@RequestParam int month){
        return ResponseEntity.ok(incomeService.getTotalByMonth(year,month));
    }
}
