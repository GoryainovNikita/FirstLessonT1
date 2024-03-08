package com.example.supplier_service.category.model;

import com.example.supplier_service.product.model.Product;
import jakarta.persistence.*;
import lombok.*;



import java.util.List;


@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "category_id")
//    @JsonManagedReference("category")
//    @JsonBackReference
    private List<Product> productSet;
}
