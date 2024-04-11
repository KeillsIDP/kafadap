package me.keills;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfiguration
public class KafkaProducer {
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducer.class, args);
    }
}