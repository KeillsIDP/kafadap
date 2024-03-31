package me.keills.controller;

import me.keills.KafkaAdapter.payload.Json;
import me.keills.kafka.KafkaJsonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/kafka")
public class MessagingController {

    @Autowired
    private KafkaJsonProducer kafkaProducer;

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
