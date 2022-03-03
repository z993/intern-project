package com.lgu.productservice.service;

import com.lgu.productservice.dto.ProductDto;
import com.lgu.productservice.jpa.ProductEntity;
import com.lgu.productservice.jpa.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        productDto.setProductId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductEntity productEntity = mapper.map(productDto, ProductEntity.class);
        productRepository.save(productEntity);
        ProductDto returnDto = mapper.map(productEntity, ProductDto.class);
        return returnDto;

    }

    @Override
    public Iterable<ProductEntity> getAllCatalog() {
        return productRepository.findAll();
    }

    @Override
    public ProductDto getProductByProductId(String productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId);
        ProductDto productDto = new ModelMapper().map(productEntity, ProductDto.class);
        return productDto;
    }


}
