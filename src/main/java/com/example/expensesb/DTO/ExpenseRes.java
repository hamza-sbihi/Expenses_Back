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
public class ExpenseRes {

    private Long id;

    private String description;

    private LocalDate date;

    private Double cost;

    private Long categoryId;

    private String categoryName;

}
