package com.example.expensesb.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name","user_id"})})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private MyUser user;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Expense> expenses = new HashSet<>();
}
