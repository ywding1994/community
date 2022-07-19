package com.ywding1994.community;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class KafkaTests {

    private static final String TOPIC = "test";

    @Resource
    private KafkaProducer kafkaProducer;

    @Test
    public void testKafka() {
        log.info("---------- Kafka: test starting... ----------");
        kafkaProducer.sendMessage(TOPIC, "你好！");
        kafkaProducer.sendMessage(TOPIC, "在吗?");

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("---------- Kafka: test ended. ----------");
    }

}

/**
 * 生产者
 */
@Component
class KafkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }

}

/**
 * 消费者
 */
@Component
class KafkaConsumer {

    @KafkaListener(topics = { "test" })
    public void handleMessage(ConsumerRecord<String, String> record) {
        System.out.println("Received: " + record.value());
    }

}
