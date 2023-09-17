package com.bruce.flink.config;

import com.bruce.common.model.FlinkCdcDataChangeInfo;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import jakarta.annotation.Resource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "flink-cdc.enable", havingValue = "true")
public class FlinkCdcConfig {

    @Resource
    private FlinkDataSourceProperties properties;

    @Bean
    public StreamExecutionEnvironment streamExecutionEnvironment(){
        return StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @Bean
    public MySqlSource<FlinkCdcDataChangeInfo> mySqlSource() {
        return MySqlSource.<FlinkCdcDataChangeInfo>builder()
                .hostname(properties.getHost())
                .port(properties.getPort())
                // 启用扫描新添加的表功能
                .scanNewlyAddedTableEnabled(true)
                // 设置捕获的数据库
                .databaseList(properties.getDatabaseList())
                // 设置捕获的表
                .tableList(properties.getTableList())
                .username(properties.getUsername())
                .password(properties.getPassword())
                //initial初始化快照,即全量导入后增量导入(检测更新数据写入)，latest:只进行增量导入(不读取历史变化)
                .startupOptions(StartupOptions.latest())
                // 将 SourceRecord 转换为 JSON 字符串
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();
    }
}
