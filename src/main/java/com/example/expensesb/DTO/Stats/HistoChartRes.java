package com.example.expensesb.DTO.Stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoChartRes {

    private String period;

    private Double totalIncome;

    private Double totalExpenses;
}
