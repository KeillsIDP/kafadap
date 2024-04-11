package me.keills.controller;

import me.keills.kafka.KafkaJsonProducer;
import me.keills.payload.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для обработки HTTP-запросов связанных с Kafka-сообщениями.
 */
@CrossOrigin
@RestController
@RequestMapping("api/kafka")
public class MessagingController {

    @Autowired
    private KafkaJsonProducer kafkaProducer;

    /**
     * Метод для публикации Kafka-сообщения.
     * @param json {@linkplain Json} объект для отправки в Kafka.
     * @return {@linkplain ResponseEntity} с сообщением об успешной отправке или ошибкой.
     */
    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Json json){
        if(json==null)
            return ResponseEntity.badRequest().body("Json is null");
        try {
            kafkaProducer.sendMessage(json);
            return ResponseEntity.ok("Message sent");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
