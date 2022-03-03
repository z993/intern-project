package com.lgu.productservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDto implements Serializable {
    private String productName;
    private Integer unitPrice;
    private String color;
    private Integer storage;
    private Integer stock;

    private String productId;


}
//Serializable json으로 변환하기 쉽다
//형식을 정해놓고 주고받을수있다
//entity에는 칼럼값이 다 있으니까 주고받을거만 정해서 받을수있다
