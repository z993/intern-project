package com.lgu.productservice.vo;

import lombok.Data;

@Data
public class RequestProduct {
    private String productName;
    private Integer unitPrice;
    private String color;
    private Integer storage;
    private Integer stock;
}
