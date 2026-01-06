package com.example.expensesb.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class IncomeSource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private MyUser user;

    @JsonIgnore
    @OneToMany(mappedBy = "incomeSource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Income> incomes = new HashSet<>();
}
