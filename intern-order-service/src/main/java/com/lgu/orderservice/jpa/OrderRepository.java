package com.lgu.orderservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    OrderEntity findByOrderId(String orderId);

//    Iterable<OrderEntity> findByUserId(String userId);

    OrderEntity findByProductId(String productId);
}