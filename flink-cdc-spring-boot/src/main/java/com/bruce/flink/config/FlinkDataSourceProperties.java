package com.bruce.flink.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Debezium数据同步配置
 */
@Data
@Configuration
@ConditionalOnProperty(name = "flink-cdc.enable", havingValue = "true")
@ConfigurationProperties(prefix = "flink-cdc.datasource")
public class FlinkDataSourceProperties {

    /**
     * MySQL 连接信息
     */
    private String host;

    /**
     * MySQL port
     */
    private Integer port;

    /**
     * MySQL user
     */
    private String username;

    /**
     * MySQL pwd
     */
    private String password;

    /**
     * 数据库列表
     */
    private String[] databaseList;

    /**
     * 数据表列表
     */
    private String[] tableList;

}
