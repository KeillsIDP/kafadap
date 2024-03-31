package me.keills.kafka;

import me.keills.KafkaAdapter.payload.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaJsonProducer {
    private final Logger LOGGER = LoggerFactory.getLogger(KafkaJsonProducer.class);
    @Value("${spring.kafka.json-topic.name}")
    private String TOPIC_NAME;
    @Autowired
    private KafkaTemplate<String, Json> kafkaTemplate;

    public void sendMessage(Json json){
        LOGGER.info(String.format("#### -> Producing message -> %s", json));

        Message<Json> message = MessageBuilder
                .withPayload(json)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();

        kafkaTemplate.send(message);
    }
}
