package me.keills.kafka;

import me.keills.payload.Json;
import me.keills.service.HttpMessengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Этот класс представляет собой Kafka-потребителя для JSON-сообщений.
 * Он потребляет JSON-сообщения из темы Kafka и отправляет их с помощью службы мессенджера HTTP.
 */
@Service
public class KafkaJsonConsumer {
    private HttpMessengerService httpMessengerService;
    private final Logger LOGGER = LoggerFactory.getLogger(KafkaJsonConsumer.class);

    /**
     * Конструктор для класса KafkaJsonConsumer.
     * @param httpMessengerService служба мессенджера HTTP для отправки запросов {@linkplain me.keills.service.HttpMessengerService}.
     */
    @Autowired
    public KafkaJsonConsumer(HttpMessengerService httpMessengerService){
        this.httpMessengerService = httpMessengerService;
    }

    /**
     * Метод слушателя Kafka, который потребляет JSON-сообщения из указанной темы.
     * Он регистрирует потребленное сообщение и отправляет его с помощью службы мессенджера HTTP.
     * @param json ({@linkplain Json })-сообщение для потребления.
     */

    @KafkaListener(topics = "${spring.kafka.json-topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(Json json){
        LOGGER.info(String.format("#### -> Consuming message -> %s", json));
        httpMessengerService.sendRequest(json);
    }
}
