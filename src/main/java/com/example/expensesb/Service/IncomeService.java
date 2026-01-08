package com.example.expensesb.Service;

import com.example.expensesb.DTO.IncomeReq;
import com.example.expensesb.DTO.IncomeRes;
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
import java.util.ArrayList;
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

    public List<IncomeRes> getAllIncomes() {
        MyUser user = getMyUser();

        List<Income> incomes = incomeRepo.findByUser(user);

        List<IncomeRes> incomesRes = new ArrayList<>();

        for(Income income : incomes){
            IncomeRes incomeRes = new IncomeRes();
            toDTO(income,incomeRes);
            incomesRes.add(incomeRes);
        }

        return incomesRes;
    }

    public IncomeRes createIncome(IncomeReq income) {
        MyUser user = getMyUser();

        IncomeSource incomeSource = incomeSourceRepo.findById(income.getIncomeSourceId())
                .orElseThrow(()-> new EntityNotFoundException("IncomeSource not found when creating income"));



        if(!Objects.equals(incomeSource.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while creating Income object unownership of IncomeSource");}

        Income incomeEntity = new Income();

        incomeEntity.setIncomeSource(incomeSource);
        incomeEntity.setUser(user);
        incomeEntity.setAmount(income.getAmount());
        incomeEntity.setDescription(income.getDescription());
        incomeEntity.setDate(income.getDate());

        incomeEntity =  incomeRepo.save(incomeEntity);

        IncomeRes incomeRes = new IncomeRes();

        toDTO(incomeEntity,incomeRes);


        return incomeRes;
    }

    public IncomeRes updateIncome(Long id, IncomeReq income) {
        MyUser user = getMyUser();

        Income incomeDb = incomeRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Income not found when updating income"));

        IncomeSource incomeSource = incomeSourceRepo.findById(income.getIncomeSourceId())
                .orElseThrow(()-> new EntityNotFoundException("IncomeSource not found when updating income"));

        //if the incomesource isn't of the user or the income wanting to change is isn't for the user we throw a forbidden
        if(!Objects.equals(incomeSource.getUser().getId(), user.getId()) || !Objects.equals(user.getId(), incomeDb.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while updating Income object");
        }

        // update values
        incomeDb.setIncomeSource(incomeSource);
        incomeDb.setAmount(income.getAmount());
        incomeDb.setDescription(income.getDescription());
        incomeDb.setDate(income.getDate());

        incomeDb =  incomeRepo.save(incomeDb);

        IncomeRes incomeRes = new IncomeRes();
        toDTO(incomeDb,incomeRes);

        return incomeRes;
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

        MyUser user = getMyUser();

        List<Income> incomes = incomeRepo.findByUser(user);

        Double total = 0.0;
        for(Income income : incomes){
            total += income.getAmount();
        }
        return total;
    }

    public List<IncomeRes> getIncomeBySource(Long id) {

        MyUser user = getMyUser();

        IncomeSource incomeSourceDb = incomeSourceRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("IncomeSource not found when getting income"));

        if(!Objects.equals(incomeSourceDb.getUser().getId(),user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while getting income By source");
        }

        List<Income> incomes = incomeRepo.findByUserAndSource(user,incomeSourceDb);

        List<IncomeRes> incomesRes = new ArrayList<>();

        for(Income income : incomes){
            IncomeRes incomeRes = new IncomeRes();
            toDTO(income,incomeRes);
            incomesRes.add(incomeRes);
        }

        return incomesRes;

    }

    public Double getTotalBySource(Long id) {

        MyUser user = getMyUser();

        IncomeSource incomeSourceDb = incomeSourceRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("IncomeSource not found when getting income"));

        if(!Objects.equals(incomeSourceDb.getUser().getId(),user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while getting income By source");
        }

        List<Income> incomes = incomeRepo.findByUserAndSource(user,incomeSourceDb);

        Double total = 0.0;
        for(Income income : incomes){
            total += income.getAmount();

        }
        return total;
    }

    public List<IncomeRes> getIncomeByMonth(int year, int month) {

        MyUser user = getMyUser();

        LocalDate start = LocalDate.of(year,month,1);
        LocalDate end = start.plusMonths(1);

        List<Income> incomes = incomeRepo.findByUserAndMonth(user,start,end);

        List<IncomeRes> incomesRes = new ArrayList<>();
        for(Income income : incomes){
            IncomeRes incomeRes = new IncomeRes();
            toDTO(income,incomeRes);
            incomesRes.add(incomeRes);
        }

        return incomesRes;
    }

    public Double getTotalByMonth(int year, int month) {

        MyUser user = getMyUser();

        LocalDate start = LocalDate.of(year,month,1);
        LocalDate end = start.plusMonths(1);

        List<Income> incomes = incomeRepo.findByUserAndMonth(user,start,end);

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

    public void toDTO(Income income,IncomeRes incomeRes){
        incomeRes.setId(income.getId());
        incomeRes.setIncomeSourceId(income.getIncomeSource().getId());
        incomeRes.setAmount(income.getAmount());
        incomeRes.setDescription(income.getDescription());
        incomeRes.setDate(income.getDate());
        incomeRes.setIncomeSourceName(income.getIncomeSource().getName());
    }

}
