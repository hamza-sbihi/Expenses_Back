package com.example.expensesb.Controller;

import com.example.expensesb.DTO.IncomeSourceReq;
import com.example.expensesb.DTO.IncomeSourceRes;
import com.example.expensesb.Entity.IncomeSource;
import com.example.expensesb.Service.IncomeSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes_sources")
public class IncomeSourceController {

    private final IncomeSourceService incomeSourceService;

    public IncomeSourceController(IncomeSourceService incomeSourceService) {
        this.incomeSourceService = incomeSourceService;
    }

    @GetMapping
    public ResponseEntity<List<IncomeSourceRes>> getAllIncomes() {
        return ResponseEntity.ok(incomeSourceService.getAll());
    }

    @PostMapping
    public ResponseEntity<IncomeSourceRes> create(@RequestBody IncomeSourceReq incomeSource) {
        return ResponseEntity.ok(incomeSourceService.create(incomeSource));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeSourceRes> update(IncomeSourceReq incomeSource, @PathVariable Long id) {
        return ResponseEntity.ok(incomeSourceService.update(incomeSource,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IncomeSource> delete(@PathVariable Long id) {
        incomeSourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
