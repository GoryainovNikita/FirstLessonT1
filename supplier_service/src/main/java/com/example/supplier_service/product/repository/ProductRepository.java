package com.example.supplier_service.product.repository;

import com.example.supplier_service.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);
    List<Product> getProductsByCategory_Id(Long id);

    List<Product> findByPrice(int price);

    List<Product> findByCategoryName(String name);

    List<Product> findProductsByPriceAfter(int price);
    List<Product> findProductsByPriceBefore(int price);

    Product findProductsByDescriptionContainsIgnoreCase(String str);
}
