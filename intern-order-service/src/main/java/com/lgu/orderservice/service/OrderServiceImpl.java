package com.lgu.orderservice.service;

import com.lgu.orderservice.dto.OrderDto;
import com.lgu.orderservice.dto.ProductDto;
import com.lgu.orderservice.jpa.OrderEntity;
import com.lgu.orderservice.jpa.OrderRepository;
import com.lgu.orderservice.vo.ResponseProduct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    Environment env;
    RestTemplate restTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, Environment env, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.env = env;
        this.restTemplate = restTemplate;
    }


    @Override
    public ProductDto getStockByProductId(String productId) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(productId);

        String productUrl = String.format("http://127.0.0.1:8100/intern-product-service/v1/%s/product", productId);

        ResponseEntity<ResponseProduct> responseProduct =
                restTemplate.exchange(productUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<ResponseProduct>() {
                        });

        ResponseProduct product = responseProduct.getBody();
        Integer stock = product.getStock();
        productDto.setStock(stock);

        return productDto;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());
        orderDto.setStatus("created");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = modelMapper.map(orderDto, OrderEntity.class);
        orderRepository.save(orderEntity);

        OrderDto returnOrderDto = modelMapper.map(orderEntity, OrderDto.class);

        return returnOrderDto;

    }
    //status바꾸기 주문완료
    @Override
    public OrderDto setOrderStatus(String orderId){
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);

        orderDto.setStatus("completed");

        return orderDto;
    }


    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);

        return orderDto;
    }


    @Override
    public Iterable<OrderEntity> getOrders() {
        return orderRepository.findAll();
    }


}
