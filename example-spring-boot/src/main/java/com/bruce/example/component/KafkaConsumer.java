package com.bruce.example.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka.enable", havingValue = "true")
public class KafkaConsumer {

    @KafkaListener(topics = "#{'${kafka.topics.my_test_topic}'}", groupId = "#{'${kafka.groups.my_test_group}'}", concurrency = "3")
    public void onMessage(ConsumerRecord<String, Object> data, Acknowledgment ack) {
        log.warn("topic：{}，partition：{}，offset：{}，value：{}", data.topic(), data.partition(), data.offset(), data.value());
        ack.acknowledge();
    }
}
