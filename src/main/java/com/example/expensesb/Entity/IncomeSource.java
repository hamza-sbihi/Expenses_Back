package com.example.expensesb.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name","user_id"})})
public class IncomeSource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private MyUser user;

    @OneToMany(mappedBy = "incomeSource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Income> incomes = new HashSet<>();
}
