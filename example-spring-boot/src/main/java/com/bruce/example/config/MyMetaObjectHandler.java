package com.bruce.example.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @description: mybatis-plus自动填充
 * @author: zoubaolu
 * @date: 2022年09月01日 13:48
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增时 自动填充更新createTime和updateTime字段时间
     *
     * @param metaObject
     * @author: zoubaolu
     * @date: 2022/9/1 13:48
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // (属性字段,填充的值,元对象)
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * 修改时 自动填充更新updateTime字段时间
     *
     * @param metaObject
     * @author: zoubaolu
     * @date: 2022/9/1 13:48
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
