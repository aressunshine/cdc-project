package com.bruce.debezium.component;

import cn.hutool.core.io.FileUtil;
import com.bruce.common.annotation.TraceLog;
import com.bruce.debezium.constant.ThreadPoolEnum;
import io.debezium.engine.DebeziumEngine;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
@PropertySource(value = "classpath:debezium.properties", encoding = "utf-8")
@ConditionalOnProperty(name = "debezium.enable", havingValue = "true")
public class DebeziumStartup implements InitializingBean, SmartLifecycle {

    @Resource
    private DebeziumEngine debeziumEngine;

    @Value("${debezium.offset-storage-file-clean}")
    private Boolean offsetStorageFileClean;

    @Value("${offset.storage.file.filename}")
    private String offsetStorageFilename;


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(debeziumEngine, "debeziumEngine must not be null");
    }

    @Override
    @TraceLog
    public void start() {
        log.info("DebeziumServerStartup, isClean: {}, fileName: {}", offsetStorageFileClean, offsetStorageFilename);
        if (offsetStorageFileClean && StringUtils.isNotBlank(offsetStorageFilename)) {
            log.warn("DebeziumServerStartup, 删除缓存文件");
            FileUtil.del(offsetStorageFilename);
        }
        log.warn(ThreadPoolEnum.DEBEZIUM_LISTENER_POOL + "线程池开始执行 debeziumEngine 实时监听任务!");
        ThreadPoolEnum.INSTANCE.getInstance().execute(debeziumEngine);
    }

    @SneakyThrows
    @Override
    public void stop() {
        debeziumEngine.close();
        log.info("debeziumEngine is stop.");
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
