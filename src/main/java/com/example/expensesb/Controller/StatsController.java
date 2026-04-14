package com.example.expensesb.Controller;

import com.example.expensesb.DTO.Stats.DonutChartRes;
import com.example.expensesb.DTO.Stats.HistoChartRes;
import com.example.expensesb.Enum.Periods;
import com.example.expensesb.Service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("expenses/by_category")
    public ResponseEntity<List<DonutChartRes>> getExpensesByCategory(@RequestParam LocalDate start,@RequestParam LocalDate end){
        return ResponseEntity.ok(statsService.getExpenseByCategory(start,end));
    }

    @GetMapping("income/by_source")
    public ResponseEntity<List<DonutChartRes>> getIncomeBySource(@RequestParam LocalDate start,@RequestParam LocalDate end){
        return ResponseEntity.ok(statsService.getIncomeBySource(start,end));
    }

    @GetMapping("histograme")
    public ResponseEntity<List<HistoChartRes>> getHistograme(@RequestParam LocalDate start, @RequestParam LocalDate end, @RequestParam Periods period){
        return ResponseEntity.ok(statsService.getHistogrameData(start,end,period));
    }
}
