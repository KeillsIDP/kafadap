package me.keills.kafka;

import me.keills.KafkaAdapter.payload.Json;
import me.keills.service.HttpMessengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaJsonConsumer {
    @Autowired
    private HttpMessengerService httpMessengerService;
    private final Logger LOGGER = LoggerFactory.getLogger(KafkaJsonConsumer.class);

    @KafkaListener(topics = "${spring.kafka.json-topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(Json json){
        LOGGER.info(String.format("#### -> Consuming message -> %s", json));
        System.out.println(httpMessengerService.sendRequest(json));
    }
}
