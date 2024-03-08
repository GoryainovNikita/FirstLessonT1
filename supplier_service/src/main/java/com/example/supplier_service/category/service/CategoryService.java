package com.example.supplier_service.category.service;

import com.example.supplier_service.category.model.Category;
import com.example.supplier_service.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public Page<Category> findPaginated(int numberPage, int sizePage){
        Pageable pageable = PageRequest.of(numberPage - 1, sizePage);
        return categoryRepository.findAll(pageable);
    }

    public Category getById(Long id){
        Optional<Category> optionalProduct = categoryRepository.findById(id);
        if(optionalProduct.isPresent()){
            return optionalProduct.get();
        }
        throw new RuntimeException("Category not found for id : " + id);
    }

    public void delete(Long id){
        categoryRepository.deleteById(id);
    }

    public Category findByName(String name){
        Category optionalCategory = categoryRepository.findByName(name);
        return optionalCategory;
    }
}
