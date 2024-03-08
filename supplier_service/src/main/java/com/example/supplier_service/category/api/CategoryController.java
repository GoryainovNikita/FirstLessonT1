package com.example.supplier_service.category.api;

import com.example.supplier_service.category.model.Category;
import com.example.supplier_service.category.model.CategoryDTO;
import com.example.supplier_service.category.model.CategoryMapper;
import com.example.supplier_service.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO){
        Category category = new Category();
        category.setName(categoryDTO.getName());
        Category save = categoryService.save(category);
        return CategoryMapper.toDTO(save);
    }

    @GetMapping
    public List<CategoryDTO> findPaginated(@RequestParam("numberPage") int numberPage){
        int pageSize = 10;
        Page<Category> categoryServicePaginated = categoryService.findPaginated(numberPage, pageSize);
        List<Category> categoryList = categoryServicePaginated.getContent();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for(var elem : categoryList){
            CategoryDTO dto = CategoryMapper.toDTO(elem);
            categoryDTOList.add(dto);
        }
        return categoryDTOList;
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable Long id){
        Category categoryServiceById = categoryService.getById(id);
        return CategoryMapper.toDTO(categoryServiceById);
    }


    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id,
                           @RequestBody CategoryDTO categoryDTO){
        Category category = CategoryMapper.to(categoryDTO);
        category.setId(id);
        return CategoryMapper.toDTO(categoryService.save(category));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        categoryService.delete(id);
    }

    @GetMapping(params = {"name"})
    public CategoryDTO getByName(@RequestParam(value = "name") String name){
        return CategoryMapper.toDTO(categoryService.findByName(name));
    }

}
