package com.example.consumer_service.service;

import com.example.consumer_service.model.Category;
import com.example.consumer_service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final RestTemplate restTemplate;

    private UriComponentsBuilder builder;

    private final String urlProduct = "http://localhost:8080/products";
    private final String urlCategories = "http://localhost:8080/categories";

    public List<Product> getProduct(int page){

        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("numberPage", page);
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Product[].class);
        Product[] products = responseEntity.getBody();
        return Arrays.stream(products).toList();
    }

    public Product getProductById(long id){
        Product product = restTemplate.getForObject(urlProduct + "/" + id, Product.class);
        return product;
    }
    public Category getCategoriesById(long id){
        Category category = restTemplate.getForObject(urlCategories + "/" + id, Category.class);
        return category;
    }
    public Category getCategoriesByName(String name){
        builder = UriComponentsBuilder.fromUri(URI.create(urlCategories)).queryParam("name", name);
        Category category = restTemplate.getForObject(builder.toUriString(), Category.class);
        return category;
    }

    public List<Category> getCategories(int page){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(urlCategories)).queryParam("numberPage", page);
        ResponseEntity<Category[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Category[].class);
        Category[] categories = responseEntity.getBody();
        return Arrays.stream(categories).toList();
    }

    public Category createNewCategory(Category category){
        ResponseEntity<Category> categoryResponseEntity = restTemplate.postForEntity(urlCategories, category, Category.class);
        return categoryResponseEntity.getBody();
    }

    public void createNewProduct(Product product, Category category){
        Category categoriesByName = getCategoriesByName(category.getName());
        Long idCategories = categoriesByName.getId();
        if(categoriesByName == null){
            Category newCategory = createNewCategory(category);
            idCategories = newCategory.getId();
        }
        product.setCategoryId(idCategories);
        restTemplate.postForObject(urlProduct, product, Void.class);
    }

    public Product getProductByName(String name){
        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("name", name);
        Product product = restTemplate.getForObject(builder.toUriString(), Product.class);
        return product;
    }

    public void deleteProduct(Long id){
        restTemplate.delete(urlProduct + "/" + id, Void.class);
    }
    public void deleteCategory(Long id){
        restTemplate.delete(urlCategories + "/" + id, Void.class);
    }

    public List<Product> getProductByCategoryId(Long id){
        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("category_id", id);
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Product[].class);
        Product[] products = responseEntity.getBody();
        return Arrays.stream(products).toList();
    }

    public List<Product> getProductByCategoryName(String name){
        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("category_name", name);
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Product[].class);
        Product[] products = responseEntity.getBody();
        return Arrays.stream(products).toList();
    }

    public List<Product> getProductByPrice(int price){
        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("price", price);
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Product[].class);
        Product[] products = responseEntity.getBody();
        return Arrays.stream(products).toList();
    }
    public List<Product> getProductByPriceMoreThen(int price){
        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("price_more", price);
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Product[].class);
        Product[] products = responseEntity.getBody();
        return Arrays.stream(products).toList();
    }
    public List<Product> getProductByPriceLessThen(int price){
        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("price_less", price);
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), Product[].class);
        Product[] products = responseEntity.getBody();
        return Arrays.stream(products).toList();
    }

    public Product getProductByDescription(String description){
        builder = UriComponentsBuilder.fromUri(URI.create(urlProduct)).queryParam("description", description);
        Product product = restTemplate.getForObject(builder.toUriString(), Product.class);
        return product;
    }


}
