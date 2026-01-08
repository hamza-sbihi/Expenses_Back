package com.example.expensesb.Service;

import com.example.expensesb.DTO.IncomeSourceReq;
import com.example.expensesb.DTO.IncomeSourceRes;
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

import java.util.ArrayList;
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

    public List<IncomeSourceRes> getAll() {

        MyUser user = getMyUser();

        List<IncomeSource> incomeSources = incomeSourceRepo.findByUser(user);

        List<IncomeSourceRes> incomeSourcesRes = new ArrayList<>();

        for (IncomeSource incomeSource : incomeSources) {
            IncomeSourceRes incomeSourceRes = new IncomeSourceRes(incomeSource.getId(),incomeSource.getName());
            incomeSourcesRes.add(incomeSourceRes);
        }

        return incomeSourcesRes;
    }

    public IncomeSourceRes create(IncomeSourceReq incomeSource) {

        MyUser user = getMyUser();

        IncomeSource incomeSourceEntity = new IncomeSource();

        incomeSourceEntity.setUser(user);

        incomeSourceEntity.setName(incomeSource.getName());

        incomeSourceEntity = incomeSourceRepo.save(incomeSourceEntity);

        return  new IncomeSourceRes(incomeSourceEntity.getId(),incomeSourceEntity.getName());

    }

    public IncomeSourceRes update(IncomeSourceReq incomeSource, Long id) {

        MyUser user = getMyUser();

        IncomeSource incomeSourceDb = incomeSourceRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IncomeSource not found with id: " + id));

        if(!Objects.equals(incomeSourceDb.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden while updating IncomeSource object");
        }

        incomeSourceDb.setName(incomeSource.getName());

        incomeSourceDb =  incomeSourceRepo.save(incomeSourceDb);

        return new IncomeSourceRes(incomeSourceDb.getId(),incomeSourceDb.getName());

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
