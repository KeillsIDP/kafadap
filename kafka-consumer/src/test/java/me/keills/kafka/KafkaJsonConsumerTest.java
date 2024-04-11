package me.keills.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import me.keills.payload.Json;
import me.keills.service.HttpMessengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@TestPropertySource(properties = {
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.group-id=test-group",
        "spring.kafka.json-topic.name=test-topic"
})
@DirtiesContext
@DisplayName("Тестирование потребителя Kafka JSON")
@Feature("Kafka")
@Story("Потребление сообщения JSON")
@Severity(SeverityLevel.CRITICAL)
class KafkaJsonConsumerTest {

    @Autowired
    private KafkaTemplate<String, Json> kafkaTemplate;

    @Mock
    private HttpMessengerService httpMessengerService;

    private KafkaJsonConsumer kafkaJsonConsumer;

    @BeforeEach
    void setUp() {
        kafkaJsonConsumer = new KafkaJsonConsumer(httpMessengerService);
    }

    @Test
    @DisplayName("Потребление сообщения")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Этот метод проверяет успешное потребление сообщения")
    void consumeMessage() throws Exception {
        String message = "{\"method\": \"POST\", \"url\": \"/users\", \"body\": \"\", \"headers\": {}, \"parameters\": {}}";
        ObjectMapper objectMapper = new ObjectMapper();
        Json json = objectMapper.readValue(message, Json.class);

        kafkaJsonConsumer.consumeMessage(json);
    }
}
