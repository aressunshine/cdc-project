package com.bruce.flink.component;

import com.bruce.common.annotation.TraceLog;
import com.bruce.common.model.FlinkCdcDataChangeInfo;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
@ConditionalOnProperty(name = "flink-cdc.enable", havingValue = "true")
public class FlinkCdcStartup implements InitializingBean, SmartLifecycle {

    @Resource
    private DataChangeSink dataChangeSink;

    @Resource
    private MySqlSource<FlinkCdcDataChangeInfo> mySqlSource;

    @Resource
    private StreamExecutionEnvironment streamExecutionEnvironment;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(dataChangeSink, "dataChangeSink must not be null");
        Assert.notNull(mySqlSource, "mySqlSource must not be null");
        Assert.notNull(streamExecutionEnvironment, "streamExecutionEnvironment must not be null");
    }

    @TraceLog
    @SneakyThrows
    @Override
    public void start() {
        log.info("启动Flink-CDC......");
        streamExecutionEnvironment.setParallelism(1);
        // 设置 3s 的 checkpoint 间隔
        streamExecutionEnvironment.enableCheckpointing(3000);
        DataStream<FlinkCdcDataChangeInfo> fromSource = streamExecutionEnvironment
                .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL-Source")
                .setParallelism(4);
        fromSource.addSink(dataChangeSink);
        streamExecutionEnvironment.execute("mysql-stream-cdc");
    }

    @SneakyThrows
    @Override
    public void stop() {
        streamExecutionEnvironment.close();
        log.info("关闭Flink-CDC......");
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
