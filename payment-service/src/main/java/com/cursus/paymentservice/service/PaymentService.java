package com.cursus.paymentservice.service;

import com.cursus.paymentservice.dtos.CartDTO;
import com.cursus.paymentservice.dtos.CartItemDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PaymentService {

    private KafkaTemplate<String, String> kafkaTemplate;

    // receive purchase request from User service
    // handling and then response status purchase was PAID or FAILED
    @KafkaListener(topics="enroll-course-topic", groupId = "enroll-group")
    public void handleEnroll(String message) {
        try{
            // Convert String message to Map
            ObjectMapper stringToMap = new ObjectMapper();
            Map<String, String> props = stringToMap.readValue(message, new TypeReference<>() {
            });
            // update status value
            props.computeIfPresent("status", (k, v) -> "PAID");

            // Convert Map of properties to String again and send it to Kafka
            ObjectMapper mapToString = new ObjectMapper();
            String mapAsString = mapToString.writeValueAsString(props);
            System.out.println(mapAsString);
            kafkaTemplate.send("transaction-status-topic", mapAsString);

        } catch (Exception e) {
            throw new RuntimeException("err when perform purchase");
        }
    }

    @KafkaListener(topics="purchase-course-topic", groupId = "purchase-group")
    public void handlePurchaseCourses(String message) {
        try{
            // Convert String message to Map
            ObjectMapper stringToObj = new ObjectMapper();
            CartDTO cartDTO = stringToObj.readValue(message, new TypeReference<>() {
            });

            double priceHaveToPay = cartDTO.getCartItems()
                    .stream()
                    .mapToDouble(CartItemDTO::getPrice)
                    .sum();
            if(cartDTO.getTotalPay() < priceHaveToPay) {
                cartDTO.setStatus("FAILED");
            }
            cartDTO.setStatus("PAID");
            // Convert Map of properties to String again and send it to Kafka
            ObjectMapper objToString = new ObjectMapper();
            String mapAsString = objToString.writeValueAsString(cartDTO);
            System.out.println(mapAsString);
            kafkaTemplate.send("purchase-status-topic", mapAsString);

        } catch (Exception e) {
            throw new RuntimeException("err when perform purchase" + e);
        }
    }
}
