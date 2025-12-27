package com.example.expensesb.Service;

import com.example.expensesb.Entity.Category;
import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Repository.CategoryRepo;
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
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final MyUserRepo myUserRepo;

    public CategoryService(CategoryRepo categoryRepo, MyUserRepo myUserRepo) {
        this.categoryRepo = categoryRepo;
        this.myUserRepo = myUserRepo;
    }

    public List<Category> getAllCategories() {

        MyUser user = getMyUser();

        return categoryRepo.findByUser(user);
    }

    public Category create(Category category) {

        MyUser user = getMyUser();

        category.setUser(user);

        return  categoryRepo.save(category);

    }

    public Category update(Category category, Long id) {

        MyUser user = getMyUser();

        Category categoryDb = categoryRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not found"));

        if(!Objects.equals(categoryDb.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        categoryDb.setName(category.getName());


        return  categoryRepo.save(categoryDb);

    }

    public void delete(Long id) {

        MyUser user = getMyUser();

        Category categoryDb =  categoryRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not found"));

        if(!Objects.equals(categoryDb.getUser().getId(), user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        categoryRepo.delete(categoryDb);

    }

    public MyUser getMyUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return myUserRepo.findByUsername(username).orElseThrow(()->new EntityNotFoundException("user not found in Category service"));
    }



}
