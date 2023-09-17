package com.bruce.debezium.constant;

public enum EventTypeEnum {
    /**
     * 增
     */
    CREATE(1),
    /**
     * 删
     */
    UPDATE(2),
    /**
     * 改
     */
    DELETE(3);

    private final int type;

    public int getType() {
        return type;
    }

    EventTypeEnum(int type) {
        this.type = type;
    }
}
