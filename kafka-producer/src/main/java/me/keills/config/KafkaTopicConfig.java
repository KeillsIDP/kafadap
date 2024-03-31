package me.keills.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@PropertySource("classpath:application.properties")
public class KafkaTopicConfig {
    @Value("${spring.kafka.json-topic.name}")
    private String jsonTopic;

    @Bean
    public NewTopic jsonTopic(){
        return TopicBuilder.name(jsonTopic)
                .build();
    }
}
