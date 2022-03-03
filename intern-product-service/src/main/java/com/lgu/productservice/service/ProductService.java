package com.lgu.productservice.service;

import com.lgu.productservice.dto.ProductDto;
import com.lgu.productservice.jpa.ProductEntity;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    Iterable<ProductEntity> getAllCatalog();

    ProductDto getProductByProductId(String productId);
}
