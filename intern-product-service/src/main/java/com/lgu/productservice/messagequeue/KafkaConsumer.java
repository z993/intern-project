package com.lgu.productservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgu.productservice.jpa.ProductEntity;
import com.lgu.productservice.jpa.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {
    ProductRepository repository;

    @Autowired
    public KafkaConsumer(ProductRepository repository) {

        this.repository = repository;
    }

    @KafkaListener(topics = "product-topic", groupId = "consumerGroupId") //groupId 추가
    public void updateQty(String kafkaMsg) {
        log.info("Kafka Message : " + kafkaMsg);

        System.out.println("got message");

        Map<Object, Object> map = new HashMap<>(); //json인줄알면 <String, String>으로 하지만 일단 어떤 형태인지 모르니까 Object
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMsg, new TypeReference<Map<Object, Object>>() {}); //읽은 메세지를 map에 넣어줌
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        ProductEntity entity = repository.findByProductId((String) map.get("productId")); 
        //map의 key=productId로해서 value를 가져와서 findByProductId에 넣어서 entity에 저장

        if (entity != null) {
            entity.setStock(entity.getStock() - (Integer) map.get("qty"));
            repository.save(entity);
        }
    }
}
