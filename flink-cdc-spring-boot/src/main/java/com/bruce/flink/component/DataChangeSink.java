package com.bruce.flink.component;

import com.bruce.common.model.FlinkCdcDataChangeInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@ConditionalOnProperty(name = "flink-cdc.enable", havingValue = "true")
public class DataChangeSink extends RichSinkFunction<FlinkCdcDataChangeInfo> {
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void invoke(FlinkCdcDataChangeInfo flinkCdcDataChangeInfo, Context context) {
        log.info("收到变更原始数据：{}", flinkCdcDataChangeInfo);
        // TODO 开始处理你的数据吧
    }
}
