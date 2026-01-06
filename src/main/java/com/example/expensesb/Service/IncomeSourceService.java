package com.example.expensesb.Service;

import com.example.expensesb.Entity.IncomeSource;
import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Repository.IncomeSourceRepo;
import com.example.expensesb.Repository.MyUserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class IncomeSourceService {

    private final IncomeSourceRepo incomeSourceRepo;
    private final MyUserRepo myUserRepo;

    public IncomeSourceService(IncomeSourceRepo incomeSourceRepo, MyUserRepo myUserRepo) {
        this.incomeSourceRepo = incomeSourceRepo;
        this.myUserRepo = myUserRepo;
    }

    public List<IncomeSource> getAll() {

        MyUser user = getMyUser();
        return incomeSourceRepo.findByUser(user);
    }

    public IncomeSource create(IncomeSource incomeSource) {

        MyUser user = getMyUser();

        incomeSource.setUser(user);

        return  incomeSourceRepo.save(incomeSource);

    }

    public IncomeSource update(IncomeSource incomeSource, Long id) {

        MyUser user = getMyUser();

        IncomeSource incomeSourceDb = incomeSourceRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IncomeSource not found with id: " + id));

        if(!Objects.equals(incomeSourceDb.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while updating IncomeSource object");
        }

        incomeSourceDb.setName(incomeSource.getName());

        return incomeSourceRepo.save(incomeSourceDb);

    }

    public void delete(Long id) {

        MyUser user = getMyUser();

        IncomeSource incomeSourceDb = incomeSourceRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IncomeSource not found with id: " + id));

        if(!Objects.equals(incomeSourceDb.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while updating IncomeSource object");
        }

        incomeSourceRepo.delete(incomeSourceDb);
    }

    public MyUser getMyUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return myUserRepo.findByUsername(username).orElseThrow(()->new EntityNotFoundException("User not found in income source service"));
    }


}
