package com.bruce.debezium.component;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.bruce.common.annotation.TraceLog;
import com.bruce.common.model.DebeziumDataChangeInfo;
import com.bruce.debezium.constant.EventTypeEnum;
import com.bruce.debezium.constant.FilterJsonFieldEnum;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName;
import static io.debezium.data.Envelope.Operation;

@Component
@ConditionalOnProperty(name = "debezium.enable", havingValue = "true")
public class DataChangeHandler {

    private static final Logger log = LoggerFactory.getLogger(DataChangeHandler.class);

    public static final String AFTER_DATA = "afterData";
    public static final String BEFORE_DATA = "beforeData";
    public static final String CHANGE_TIME = "changeTime";
    public static final String EVENT_TYPE = "eventType";
    public static final String SOURCE = "source";

    public static final String TABLE = "table";
    private static final List<Operation> OPERATION_LIST = List.of(Operation.CREATE, Operation.UPDATE, Operation.DELETE);


    @TraceLog
    public void handlePayload(List<RecordChangeEvent<SourceRecord>> recordChangeEvents,
                              DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordCommitter) {
        for (RecordChangeEvent<SourceRecord> r : recordChangeEvents) {
            SourceRecord sourceRecord = r.record();
            Struct sourceRecordChangeValue = (Struct) sourceRecord.value();
            if (sourceRecordChangeValue == null) {
                log.warn("DebeziumChangeHandler: sourceRecordChangeValue is null!");
                continue;
            }
            Object obj = null;
            try {
                obj = sourceRecordChangeValue.get(FieldName.OPERATION);
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }
            Operation operation = Operation.forCode((String) obj);
            log.warn("operation: {}", operation);
            if (Objects.isNull(operation)) {
                log.warn("DebeziumChangeHandler: operation is null!");
                continue;
            }
            Map<String, Object> changeMap = getChangeTableInfo(sourceRecordChangeValue);
            if (MapUtils.isEmpty(changeMap)) {
                log.warn("DebeziumChangeHandler: changeMap is empty!");
                continue;
            }
            if (!changeMap.containsKey(TABLE)) {
                log.warn("DebeziumChangeHandler: changeMap is not contain table!");
                continue;
            }
            if (!OPERATION_LIST.contains(operation)) {
                log.warn("DebeziumChangeHandler: operationList is not contain operation!");
                continue;
            }
            DebeziumDataChangeInfo debeziumDataChangeInfo = getChangeDataInfo(sourceRecordChangeValue, operation, changeMap);
            if (Objects.isNull(debeziumDataChangeInfo)) {
                log.warn("DebeziumChangeHandler: debeziumDataChangeInfo is null!");
                continue;
            }
            log.info("发送变更数据：{}", JSON.toJSONString(debeziumDataChangeInfo));
        }
        try {
            recordCommitter.markBatchFinished();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取表变更信息
     *
     * @param sourceRecordChangeValue 变更值
     * @return
     */
    private Map<String, Object> getChangeTableInfo(Struct sourceRecordChangeValue) {
        Struct struct = (Struct) sourceRecordChangeValue.get(SOURCE);
        Map<String, Object> map = struct.schema().fields().stream()
                .map(Field::name)
                .filter(fieldName -> Objects.nonNull(struct.get(fieldName)) && FilterJsonFieldEnum.filterJsonField(fieldName))
                .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        if (map.containsKey(FilterJsonFieldEnum.ts_ms.name())) {
            map.put(CHANGE_TIME, map.get(FilterJsonFieldEnum.ts_ms.name()));
            map.remove(FilterJsonFieldEnum.ts_ms.name());
        }
        return map;
    }

    /**
     * 获取数据变更信息
     *
     * @param sourceRecordChangeValue 变更值
     * @param operation               操作类型
     * @return
     */
    private DebeziumDataChangeInfo getChangeDataInfo(Struct sourceRecordChangeValue, Operation operation, Map<String, Object> changeMap) {
        // 操作类型过滤,只处理增删改
        String tableName = String.valueOf(changeMap.get(TABLE).toString());
        log.warn("DebeziumChangeHandler: getChangeDataInfo-tableName is {}!", tableName);
        EventTypeEnum eventTypeEnum;
        Map<String, Object> result = new HashMap<>(4);
        if (Objects.equals(Operation.CREATE, operation)) {
            eventTypeEnum = EventTypeEnum.CREATE;
            result.put(AFTER_DATA, getChangeData(sourceRecordChangeValue, FieldName.AFTER));
            result.put(BEFORE_DATA, null);
        } else if (Objects.equals(Operation.UPDATE, operation)) {
            eventTypeEnum = EventTypeEnum.UPDATE;
            result.put(AFTER_DATA, getChangeData(sourceRecordChangeValue, FieldName.AFTER));
            result.put(BEFORE_DATA, getChangeData(sourceRecordChangeValue, FieldName.BEFORE));
        } else if (Objects.equals(Operation.DELETE, operation)) {
            eventTypeEnum = EventTypeEnum.DELETE;
            result.put(AFTER_DATA, getChangeData(sourceRecordChangeValue, FieldName.BEFORE));
            result.put(BEFORE_DATA, getChangeData(sourceRecordChangeValue, FieldName.BEFORE));
        } else {
            log.warn("DebeziumChangeHandler: getChangeDataInfo-operation is not support!");
            return null;
        }
        result.put(EVENT_TYPE, eventTypeEnum.getType());
        result.putAll(changeMap);
        return BeanUtil.copyProperties(result, DebeziumDataChangeInfo.class);
    }

    public String getChangeData(Struct sourceRecordChangeValue, String record) {
        Struct struct = (Struct) sourceRecordChangeValue.get(record);
        Map<String, Object> changeData = struct.schema().fields().stream()
                .map(Field::name)
                .filter(fieldName -> struct.get(fieldName) != null)
                .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        if (MapUtils.isEmpty(changeData)) {
            return null;
        }
        return JSON.toJSONString(changeData);
    }
}
