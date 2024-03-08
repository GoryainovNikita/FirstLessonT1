package com.example.supplier_service.category.repository;

import com.example.supplier_service.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByName (String name);
}
