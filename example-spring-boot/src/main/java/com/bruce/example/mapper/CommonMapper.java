package com.bruce.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CommonMapper接口
 *
 * @author Brucezou
 */
public interface CommonMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param insertList 插入列表
     */
    void insertBatchSomeColumn(@Param("list") List<T> insertList);
}
