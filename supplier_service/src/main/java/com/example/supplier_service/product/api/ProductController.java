package com.example.supplier_service.product.api;

import com.example.supplier_service.category.model.Category;
import com.example.supplier_service.product.model.Product;
import com.example.supplier_service.product.model.ProductDTO;
import com.example.supplier_service.product.model.ProductMapper;
import com.example.supplier_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO){
        Product product = ProductMapper.to(productDTO);
        Category category = new Category();
        category.setId(productDTO.getCategoryId());
        category.setName(productDTO.getCategoryName());
        product.setCategory(category);
        return ProductMapper.toDTO(productService.save(product));
    }

    @GetMapping
    public List<ProductDTO> findPaginated(@RequestParam("numberPage") int numberPage){
        int pageSize = 10;
        Page<Product> taskServicePaginated = productService.findPaginated(numberPage, pageSize);
        List<Product> productList = taskServicePaginated.getContent();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(var elem : productList){
            ProductDTO dto = ProductMapper.toDTO(elem);
            productDTOS.add(dto);
        }
        return productDTOS;
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Long id){
        Product product = productService.getById(id);
        ProductDTO productDTO = ProductMapper.toDTO(product);
        return productDTO;
    }


    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id,
                          @RequestBody Product product){
        product.setId(id);
        return ProductMapper.toDTO(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        productService.delete(id);
    }

    @GetMapping(params = {"name"})
    public ProductDTO getByName(@RequestParam(value = "name") String name){
        return ProductMapper.toDTO(productService.findByName(name));
    }

    @GetMapping(params = {"category_id"})
    public List<ProductDTO> getByName(@RequestParam(value = "category_id") Long categoryId){
        List<Product> byCategoryId = productService.getByCategoryId(categoryId);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(var elem : byCategoryId){
            ProductDTO productDTO = ProductMapper.toDTO(elem);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }
}
