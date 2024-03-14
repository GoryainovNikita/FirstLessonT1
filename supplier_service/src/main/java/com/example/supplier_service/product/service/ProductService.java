package com.example.supplier_service.product.service;

import com.example.supplier_service.product.model.Product;
import com.example.supplier_service.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product save(Product product){
        return productRepository.save(product);
    }

    public Page<Product> findPaginated(int numberPage, int sizePage){
        Pageable pageable = PageRequest.of(numberPage - 1, sizePage);
        return productRepository.findAll(pageable);
    }

    public Product getById(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            return optionalProduct.get();
        }
        throw new RuntimeException("Task not found for id : " + id);
    }

    public void delete(Long id){
        productRepository.deleteById(id);
    }

    public Product findByName(String name){
        Product product = productRepository.findByName(name);
        return product;
    }

    public List<Product> getByCategoryId(Long id){
       return productRepository.getProductsByCategory_Id(id);
    }

    public List<Product> getByPrice(int price){
        return productRepository.findByPrice(price);
    }

    public List<Product> getByCategoryName(String name){
        return productRepository.findByCategoryName(name);
    }

    public List<Product> getByPriceWherePriceMoreThen(int price){
        return productRepository.findProductsByPriceAfter(price);
    }

    public List<Product> getByPriceWherePriceLessThen(int price){
        return productRepository.findProductsByPriceBefore(price);
    }

    public Product findByDescription(String name){
        Product product = productRepository.findProductsByDescriptionContainsIgnoreCase(name);
        return product;
    }

}
