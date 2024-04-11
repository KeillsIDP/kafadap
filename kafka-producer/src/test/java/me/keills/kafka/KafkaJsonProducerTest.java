package me.keills.kafka;

import io.qameta.allure.*;
import me.keills.payload.Json;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext
@TestPropertySource(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
})
@Feature("Kafka")
@Story("Отправка сообщения JSON")
@Severity(SeverityLevel.CRITICAL)
class KafkaJsonProducerTest {
    EmbeddedKafkaBroker kafkaEmbedded;
    KafkaJsonProducer kafkaJsonProducer;
    Consumer<String, Json> consumer;
    final String TOPIC = "json-kafka";

    @Autowired
    KafkaJsonProducerTest(EmbeddedKafkaBroker kafkaEmbedded, KafkaJsonProducer kafkaJsonProducer){
        this.kafkaEmbedded = kafkaEmbedded;
        this.kafkaJsonProducer = kafkaJsonProducer;

        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", kafkaEmbedded));
        JsonDeserializer deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        consumer = new DefaultKafkaConsumerFactory<String, Json>(configs, new StringDeserializer(), deserializer).createConsumer();
        consumer.subscribe(List.of(TOPIC));
    }

    @Test
    @DisplayName("Отправка JSON сообщения")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Этот метод проверяет успешную отправку JSON сообщения и его корректное получение")
    void sendJsonMessage() {
        // given
        Json json = new Json("POST", "/users", "", new HashMap<>(), new HashMap<>());  // create the appropriate Json object

        // when
        kafkaJsonProducer.sendMessage(json);

        ConsumerRecord<String, Json> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        assertNotNull(singleRecord);
        assertEquals("POST",singleRecord.value().getMethod());
        assertEquals("/users", singleRecord.value().getUrl());
        assertEquals("", singleRecord.value().getBody());
        assertEquals(0, singleRecord.value().getHeaders().size());
        assertEquals(0, singleRecord.value().getParameters().size());
    }
}