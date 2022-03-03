package com.lgu.productservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProduct {

    private String productName;
    private Integer unitPrice;
    private String color;
    private Integer storage;
    private Integer stock;
    private Date createdAt;

    private String productId;
}
