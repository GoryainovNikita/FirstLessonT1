package com.example.consumer_service.model;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class Product {
    private Long id;

    private String name;

    private String description;

    private int price;

    private String categoryName;

    private Long categoryId;

}
