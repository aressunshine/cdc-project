package com.bruce.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.example.domain.UserInfo;

/**
* @author zoubaolu
* @description 针对表【user_info】的数据库操作Service
* @createDate 2023-09-13 23:41:24
*/
public interface UserInfoService extends IService<UserInfo> {

    String test();
}
