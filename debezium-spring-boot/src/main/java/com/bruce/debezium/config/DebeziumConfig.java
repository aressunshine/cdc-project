package com.bruce.debezium.config;

import com.bruce.debezium.component.DataChangeHandler;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

@Configuration
@ConditionalOnProperty(name = "debezium.enable", havingValue = "true")
public class DebeziumConfig {

    @Resource
    private DataChangeHandler dataChangeHandler;

    @SneakyThrows
    @Bean(name = "debeziumProperties")
    Properties debeziumProperties() {
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource("debezium.properties"));
    }


    @Bean(name = "debeziumEngine")
    DebeziumEngine debeziumEngine() {
        return DebeziumEngine
                .create(ChangeEventFormat.of(Connect.class))
                .using(debeziumProperties())
                .notifying(dataChangeHandler::handlePayload)
                .build();
    }
}
