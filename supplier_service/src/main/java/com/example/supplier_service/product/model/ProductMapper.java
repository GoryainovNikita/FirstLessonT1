package com.example.supplier_service.product.model;


public final class ProductMapper {

    public static ProductDTO toDTO(Product product){
      ProductDTO productDTO = new ProductDTO();
      productDTO.setId(product.getId());
      productDTO.setName(product.getName());
      productDTO.setPrice(product.getPrice());
      productDTO.setDescription(product.getDescription());
      productDTO.setCategoryName(product.getCategory().getName());
      productDTO.setCategoryId(product.getCategory().getId());
      return productDTO;
    }

    public static Product to(ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        return product;
    }

}
