package com.example.supplier_service.category.model;

public final class CategoryMapper {

    public static CategoryDTO toDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        try{
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return categoryDTO;
    }

    public static Category to(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setId(categoryDTO.getId());
        return category;
    }
}
