package com.bruce.flink.config;

import com.alibaba.fastjson2.JSONObject;
import com.bruce.common.model.FlinkCdcDataChangeInfo;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JsonDebeziumDeserializationSchema implements DebeziumDeserializationSchema<FlinkCdcDataChangeInfo> {

    public static final String TS_MS = "ts_ms";
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String SOURCE = "source";
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";


    /**
     * Deserialize the Debezium record, it is represented in Kafka {@link SourceRecord}.
     *
     * @param sourceRecord
     * @param collector
     */
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<FlinkCdcDataChangeInfo> collector) {
        try {
            String topic = sourceRecord.topic();
            String[] fields = topic.split("\\.");
            String database = fields[1];
            String tableName = fields[2];
            Struct struct = (Struct) sourceRecord.value();
            final Struct source = struct.getStruct(SOURCE);
            FlinkCdcDataChangeInfo flinkCdcDataChangeInfo = new FlinkCdcDataChangeInfo();
            flinkCdcDataChangeInfo.setBeforeData(getJsonObject(struct, BEFORE).toJSONString());
            flinkCdcDataChangeInfo.setAfterData(getJsonObject(struct, AFTER).toJSONString());
            // 获取操作类型  CREATE UPDATE DELETE  1新增 2修改 3删除
            Envelope.Operation operation = Envelope.operationFor(sourceRecord);
            String type = operation.toString().toUpperCase();
            int eventType = type.equals(CREATE) ? 1 : UPDATE.equals(type) ? 2 : 3;
            flinkCdcDataChangeInfo.setEventType(eventType);
            flinkCdcDataChangeInfo.setDatabase(database);
            flinkCdcDataChangeInfo.setTableName(tableName);
            ZoneId zone = ZoneId.systemDefault();
            Long timestamp = Optional.ofNullable(struct.get(TS_MS))
                    .map(x -> Long.parseLong(x.toString()))
                    .orElseGet(System::currentTimeMillis);
            flinkCdcDataChangeInfo.setChangeTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zone));
            //7.输出数据
            collector.collect(flinkCdcDataChangeInfo);
        } catch (Exception e) {
            log.error("MySQL-Server消息读取自定义序列化报错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public TypeInformation<FlinkCdcDataChangeInfo> getProducedType() {
        return TypeInformation.of(FlinkCdcDataChangeInfo.class);
    }

    /**
     * 从源数据获取出变更之前或之后的数据
     */
    private JSONObject getJsonObject(Struct value, String fieldElement) {
        Struct element = value.getStruct(fieldElement);
        JSONObject jsonObject = new JSONObject();
        if (element != null) {
            Schema afterSchema = element.schema();
            List<Field> fieldList = afterSchema.fields();
            for (Field field : fieldList) {
                Object afterValue = element.get(field);
                jsonObject.put(field.name(), afterValue);
            }
        }
        return jsonObject;
    }
}
