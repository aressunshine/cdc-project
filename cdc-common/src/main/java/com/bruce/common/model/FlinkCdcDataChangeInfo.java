package com.bruce.common.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlinkCdcDataChangeInfo {

    /**
     * 数据库名
     */
    private String database;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 变更类型 1新增 2修改 3删除
     */
    private Integer eventType;
    /**
     * 变更前数据
     */
    private String beforeData;
    /**
     * 变更后数据
     */
    private String afterData;
    /**
     * 变更时间
     */
    private LocalDateTime changeTime;
}
