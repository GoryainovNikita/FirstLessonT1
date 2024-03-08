package com.example.supplier_service.product.model;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    private String name;

    private String description;

    private int price;

    private String categoryName;

    private Long categoryId;
}
