package com.lgu.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgu.orderservice.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> KafkaTemplate){

        this.kafkaTemplate = KafkaTemplate;
    }


    public OrderDto send(String topic, OrderDto orderDto){
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try{
            jsonInString = mapper.writeValueAsString(orderDto);

        } catch(JsonProcessingException ex){
            ex.printStackTrace();
        }

        log.info("Kafka Producer sent data from Order MS11" + orderDto);
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from Order MS22" + orderDto);

        return orderDto;

    }
}
