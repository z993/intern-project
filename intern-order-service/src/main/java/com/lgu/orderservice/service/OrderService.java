package com.lgu.orderservice.service;

import com.lgu.orderservice.dto.OrderDto;
import com.lgu.orderservice.dto.ProductDto;
import com.lgu.orderservice.jpa.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderByOrderId(String orderId);

    //status바꾸기 추기
    OrderDto setOrderStatus(String orderId);

    ProductDto getStockByProductId(String productId);

    Iterable<OrderEntity> getOrders();
}
