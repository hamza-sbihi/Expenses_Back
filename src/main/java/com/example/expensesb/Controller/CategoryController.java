package com.example.expensesb.Controller;

import com.example.expensesb.DTO.CategoryReq;
import com.example.expensesb.DTO.CategoryRes;
import com.example.expensesb.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryRes>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping()
    public ResponseEntity<CategoryRes> create(@RequestBody CategoryReq category){
        return ResponseEntity.ok(categoryService.create(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryRes> update(@RequestBody CategoryReq category, @PathVariable Long id){
        return ResponseEntity.ok(categoryService.update(category,id));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
