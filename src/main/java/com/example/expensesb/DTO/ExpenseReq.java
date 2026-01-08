package com.example.expensesb.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseReq {

    private String description;

    private LocalDate date;

    private Double cost;

    private Long categoryId;
}
