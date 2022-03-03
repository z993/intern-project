package com.lgu.orderservice.controller;

import com.lgu.orderservice.dto.OrderDto;
import com.lgu.orderservice.dto.ProductDto;
import com.lgu.orderservice.jpa.OrderEntity;
import com.lgu.orderservice.messagequeue.KafkaProducer;
import com.lgu.orderservice.service.OrderService;
import com.lgu.orderservice.vo.RequestOrder;
import com.lgu.orderservice.vo.ResponseOrder;
import com.lgu.orderservice.vo.ResponseProduct;
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
@RequestMapping("/intern-order-service")
public class OrderController {
    Environment env;
    OrderService orderService;
    KafkaProducer kafkaProducer;

    @Autowired
    public OrderController(Environment env, OrderService orderService, KafkaProducer kafkaProducer) {
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/v1/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s", env.getProperty("local.server.port"));
    }


    // product-service에 상품 재고 조회 요청
    @GetMapping("/v1/product/{productId}")
    public ResponseEntity<ResponseProduct> getStock(@PathVariable("productId") String productId) {
        ProductDto productDto = orderService.getStockByProductId(productId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseProduct responseProduct = mapper.map(productDto, ResponseProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseProduct);
    }


    // 주문 생성
    @PostMapping("/v1/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder orderDetails) {


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderService.createOrder(orderDto);

        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }


    // 주문 완료 -> kafka 메시지 발행
    @PatchMapping("/v1/{orderId}")
    public ResponseEntity<ResponseOrder> completeOrder(@PathVariable("orderId") String orderId) {
        OrderDto orderDto = orderService.setOrderStatus(orderId);
        ResponseOrder responseOrder = new ModelMapper().map(orderDto, ResponseOrder.class);

        /* send order to the kafka */
        kafkaProducer.send("product-topic", orderDto);
        System.out.println("message sent");


        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);

    }


    @GetMapping("/v1/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders() {
        Iterable<OrderEntity> orderList = orderService.getOrders();

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}