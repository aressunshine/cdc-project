package com.bruce.common.model;


import lombok.Data;

@Data
public class DebeziumDataChangeInfo {
    /**
     * 数据库名
     */
    private String db;
    /**
     * 表名
     */
    private String table;
    /**
     * 操作类型，1：add；2：update，3：delete
     */
    private Integer eventType;
    /**
     * 操作时间
     */
    private Long changeTime;
    /**
     * 变更后数据
     */
    private String afterData;
    /**
     * 变更前数据
     */
    private String beforeData;
}
