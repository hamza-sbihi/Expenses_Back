package com.example.expensesb.Service;

import com.example.expensesb.Entity.Income;
import com.example.expensesb.Entity.IncomeSource;
import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Repository.IncomeRepo;
import com.example.expensesb.Repository.IncomeSourceRepo;
import com.example.expensesb.Repository.MyUserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class IncomeService {


    private final IncomeRepo incomeRepo;
    private final MyUserRepo myUserRepo;
    private final IncomeSourceRepo incomeSourceRepo;

    public IncomeService(IncomeRepo incomeRepo, MyUserRepo myUserRepo, IncomeSourceRepo incomeSourceRepo) {
        this.incomeRepo = incomeRepo;
        this.myUserRepo = myUserRepo;
        this.incomeSourceRepo = incomeSourceRepo;
    }

    public List<Income> getAllIncomes() {
        MyUser user = getMyUser();

        return incomeRepo.findByUser(user);
    }

    public Income createIncome(Income income) {
        MyUser user = getMyUser();

        IncomeSource incomeSource = incomeSourceRepo.findById(income.getIncomeSource().getId())
                .orElseThrow(()-> new EntityNotFoundException("IncomeSource not found when creating income"));

        income.setIncomeSource(incomeSource);
        income.setUser(user);

        if(!Objects.equals(incomeSource.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while creating Income object unownership of IncomeSource");
        }
        return incomeRepo.save(income);
    }

    public Income updateIncome(Long id, Income income) {
        MyUser user = getMyUser();

        Income incomeDb = incomeRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Income not found when updating income"));

        IncomeSource incomeSource = incomeSourceRepo.findById(income.getIncomeSource().getId())
                .orElseThrow(()-> new EntityNotFoundException("IncomeSource not found when updating income"));

        //if the incomesource isnt of the user or the income wanting to change is isnt for the user we throw a forbidden
        if(!Objects.equals(incomeSource.getUser().getId(), user.getId()) || !Objects.equals(user.getId(), incomeDb.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while updating Income object");
        }

        // update values
        incomeDb.setIncomeSource(incomeSource);
        incomeDb.setAmount(income.getAmount());
        incomeDb.setDescription(income.getDescription());
        incomeDb.setDate(income.getDate());

        return incomeRepo.save(incomeDb);
    }

    public void delete(Long id) {

        MyUser user = getMyUser();

        Income incomeDb = incomeRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Income not found when updating income"));

        if(!Objects.equals(incomeDb.getUser().getId(),user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while deleting Income object ");
        }

        incomeRepo.delete(incomeDb);

    }

    public Double getTotal() {
        List<Income> incomes = getAllIncomes();

        Double total = 0.0;
        for(Income income : incomes){
            total += income.getAmount();
        }
        return total;
    }

    public List<Income> getIncomeBySource(Long id) {

        MyUser user = getMyUser();

        IncomeSource incomeSourceDb = incomeSourceRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("IncomeSource not found when getting income"));

        if(!Objects.equals(incomeSourceDb.getUser().getId(),user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while getting income By source");
        }

        return incomeRepo.findByUserAndSource(user,incomeSourceDb);

    }

    public Double getTotalBySource(Long id) {

        List<Income> incomes = getIncomeBySource(id);

        Double total = 0.0;
        for(Income income : incomes){
            total += income.getAmount();

        }
        return total;
    }

    public List<Income> getIncomeByMonth(int year, int month) {

        MyUser user = getMyUser();

        LocalDate start = LocalDate.of(year,month,1);
        LocalDate end = start.plusMonths(1);

        return incomeRepo.findByUserAndMonth(user,start,end);
    }

    public Double getTotalByMonth(int year, int month) {

        List<Income> incomes =  getIncomeByMonth(year,month);

        Double total = 0.0;
        for(Income income : incomes){
            total += income.getAmount();
        }
        return total;
    }

    public MyUser getMyUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return myUserRepo.findByUsername(username).orElseThrow(()->new EntityNotFoundException("user not found in Income service"));
    }

}
