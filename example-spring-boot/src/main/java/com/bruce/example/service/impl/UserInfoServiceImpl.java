package com.bruce.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.example.domain.UserInfo;
import com.bruce.example.mapper.UserInfoMapper;
import com.bruce.example.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author zoubaolu
 * @description 针对表【user_info】的数据库操作Service实现
 * @createDate 2023-09-13 23:41:24
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    int a = 0;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Retryable(recover = "recoverTest", retryFor = {RuntimeException.class}, maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    public String test() {
        a++;
        System.out.println(a + " - " + System.currentTimeMillis());
        if (a < 10) {
            throw new RuntimeException("未满足条件");
        }
        return "执行成功";
    }

    @Recover
    public String recoverTest(RuntimeException e) {
        return "回调方法-" + e.getMessage();
    }
}




