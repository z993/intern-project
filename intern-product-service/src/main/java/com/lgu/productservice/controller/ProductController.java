package com.lgu.productservice.controller;

import com.lgu.productservice.dto.ProductDto;
import com.lgu.productservice.jpa.ProductEntity;
import com.lgu.productservice.service.ProductService;
import com.lgu.productservice.vo.RequestProduct;
import com.lgu.productservice.vo.ResponseProduct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/intern-product-service")
public class ProductController {
    Environment env;
    ProductService productService;


    @Autowired
    public ProductController(Environment env, ProductService productService) {
        this.env = env;
        this.productService = productService;
    }


    @GetMapping("/v1/health_check")
    public String status() {
        return String.format("It's Working in Catalog Service on PORT %s", env.getProperty("local.server.port"));
    }


    @PostMapping("/v1/products")
    public ResponseEntity<ResponseProduct> createProduct(@RequestBody RequestProduct productDetails){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductDto productDto = mapper.map(productDetails, ProductDto.class);
        ProductDto createDto = productService.createProduct(productDto);
        ResponseProduct responseProduct = mapper.map(createDto, ResponseProduct.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseProduct);
    }


    @GetMapping("/v1/products")
    public ResponseEntity<List<ResponseProduct>> getProducts(){
        Iterable<ProductEntity> productList = productService.getAllCatalog();
        List<ResponseProduct> result = new ArrayList<>();
        productList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseProduct.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/v1/{productId}/product")
    public ResponseEntity<ResponseProduct> getProduct(@PathVariable("productId") String productId){

        ProductDto productDto = productService.getProductByProductId(productId);
        ResponseProduct returnValue = new ModelMapper().map(productDto, ResponseProduct.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }


}