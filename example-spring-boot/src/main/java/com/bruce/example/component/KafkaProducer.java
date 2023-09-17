package com.bruce.example.component;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, String key, Object obj) {
        String message = JSON.toJSONString(obj);
        log.info("Send message to kafka, topic: {}, key: {}, message: {}", topic, key, message);
        CompletableFuture<SendResult<String, Object>> completableFuture = kafkaTemplate.send(topic, key, message);
        // 发送成功
        completableFuture.thenAccept(result -> log.debug("Send success! Message: {}", message));
        // 发送失败
        completableFuture.exceptionally(e -> {
            log.error("Send failed! Message: {}", message, e);
            return null;
        });
    }
}
