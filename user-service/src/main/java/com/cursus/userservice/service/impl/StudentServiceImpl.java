package com.cursus.userservice.service.impl;

import com.cursus.userservice.config.feignClient.CourseFeignClient;
import com.cursus.userservice.dto.CartDTO;
import com.cursus.userservice.entity.Student;
import com.cursus.userservice.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private KafkaTemplate<String, String> kafkaTemplate;

    private ConsumerFactory<String, String> consumerFactoryConfig;

    private CourseFeignClient courseFeignClient;


    @Override
    public void viewCourses(Student student) {

    }

    @Override
    public void viewInstructors(Student student) {

    }

    // STEP 1
    // Check dup, if User already purchase the course or not
    // send purchase request to Payment service
    // and waiting for purchase status response
    // STEP 2
    // Receive purchase status response
    // Status maybe PAID or FAILED
    // If PAID then proceed request to Course Controller
    // Call Course Controller directly using feign client
    // If FAILED then send request to Notification Service
    // STEP 3
    // receive assigned success status from Course Service
    // push notification to user
    @Override
    public void enrollCourse(String studentId, String courseId) {
        try {
            String transactionId = initProcess(studentId, courseId);
        } catch (Exception e) {
            System.out.println("transaction fail : " + e);
        }
    }

    public String initProcess(String studentId, String courseId) {
        String topic = "enroll-course-topic";
        String transactionId = UUID.randomUUID().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> props = new HashMap<>() {{
                put("transactionId", transactionId);
                put("studentId", studentId);
                put("courseId", courseId);
                put("status", "PENDING");
            }};
            String mapAsString = objectMapper.writeValueAsString(props);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, mapAsString);
            System.out.println("STEP 1 : Check dup, if User already purchase the course or not...");
            Producer<String, String> producer = kafkaTemplate.getProducerFactory().createProducer();

            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    throw new RuntimeException("err sending message to kafka " + exception.getMessage());
                } else {
                    // Successful message sent
                    System.out.printf("Message sent to topic %s, partition %d, offset %d%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
            producer.flush();
            producer.close();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("err when cast map to string");
        }
        System.out.println("INIT PROCESS SUCCESSFULLY");
        return transactionId;
    }

    @KafkaListener(topics = "transaction-status-topic", groupId = "step2")
    public boolean waitForPaymentStatus(String message) {
        boolean result = false;
        System.out.println("message= " + message);
        try {
            // Convert String message to Map
            ObjectMapper stringToMap = new ObjectMapper();
            Map<String, Object> props = stringToMap.readValue(message, new TypeReference<>() {
            });
            if (props.get("status").equals("PAID")) {
                // Call Course Controller directly
                try {
                    courseFeignClient.assignStudentToCourse(String.valueOf(props.get("studentId")), String.valueOf(props.get("courseId")));
                    result = true;
                } catch (Exception e) {
                    System.out.println("err calling course service : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("err when pay course :" + e.getMessage());
        }
        System.out.println("PAY SUCCESSFULLY");
        return result;
    }

//    @KafkaListener(topics = "transaction-status-topic", groupId = "status-group")
//    public void test(ConsumerRecord<String, String> record) {
//        System.out.printf("Received status update: %s", record.value());
//    }
//    @KafkaListener(topics = "transaction-status-topic", groupId = "enroll-group")
//    private void receiveEnrollStatus(String message) {
//        try{
//            // Convert String message to Map
//            ObjectMapper stringToMap = new ObjectMapper();
//            Map<String, Object> props = stringToMap.readValue(message, new TypeReference<>() {
//            });
//            if (props.get("transactionId").equals("PAID")) {
//                System.out.println("STEP 2: Assigning Student to Course...");
//                // Call Course Controller directly
//                try {
//                    courseFeignClient.assignStudentToCourse(String.valueOf(props.get("studentId")), String.valueOf(props.get("courseId")));
//                } catch (Exception e) {
//                    System.out.println("err calling course service : " + e.getMessage());
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("err when assigning course");
//        }
//    }

    @KafkaListener(topics = "notification-topic", groupId = "step4")
    private boolean waitForEnrollmentStatus(ConsumerRecord<String, String> record) {
        boolean result = false;
        try {
            // Convert String message to Map
            ObjectMapper stringToMap = new ObjectMapper();
            Map<String, Object> props = stringToMap.readValue(record.value(), new TypeReference<>() {
            });
            if (props.get("status").equals("ASSIGNED")) {
                result = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("err when assigning course");
        }
        System.out.println("ENROLL SUCCESSFULLY");
        return result;
    }

    @Override
    public void reportCourse(Student student, String courseId, String reportReason) {

    }

    @Override
    public void purchaseCourses(CartDTO cartDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String mapAsString = objectMapper.writeValueAsString(cartDTO);
            kafkaTemplate.send("purchase-course-topic", mapAsString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("cannot proceed enroll course" + e);
        }
    }


    @KafkaListener(topics = "purchase-status-topic", groupId = "purchase-group")
    private void receivePurchaseStatus(String message) {
        try {
            // Convert String message to Map
            ObjectMapper stringToObj = new ObjectMapper();
            CartDTO cartDTO = stringToObj.readValue(message, new TypeReference<>() {
            });
            System.out.println(cartDTO.getStatus());
            if (cartDTO.getStatus().equals("PAID")) {
                System.out.println("STEP 2: Assigning Student to Course...");
                // Call Course Controller directly
                try {
                    cartDTO.getCartItems().forEach(
                            cartItemDTO ->
                                    courseFeignClient.assignStudentToCourse(
                                            cartDTO.getStudentId(),
                                            cartItemDTO.getCourseId()
                                    ));

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("err when assigning course");
        }
    }

    @Override
    public void viewCart(String studentId) {

    }

    @Override
    public void addCourseToCart(String studentId, String courseId) {

    }

    @Override
    public void removeCourseFromCart(String studentId, String courseId) {

    }

}
