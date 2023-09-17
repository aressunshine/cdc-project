package com.bruce.example.controller;

import com.bruce.example.domain.UserInfo;
import com.bruce.example.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_info")
@Tag(name = "用户信息-控制器")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户")
    @Parameter(name = "id", required = true, in = ParameterIn.PATH)
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long id) {
        UserInfo userInfo = userInfoService.getById(id);
        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除用户", parameters = {@Parameter(name = "id", required = true, in = ParameterIn.PATH)})
    public ResponseEntity<UserInfo> deleteUserById(@PathVariable Long id) {
        userInfoService.removeById(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/")
    @Operation(summary = "新增用户")
    public ResponseEntity<UserInfo> body(@RequestBody UserInfo userInfo) {
        userInfoService.save(userInfo);
        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "根据用户ID更新用户")
    @Parameters({@Parameter(name = "id", description = "用户id", required = true, in = ParameterIn.PATH)})
    @PutMapping("/{id}")
    public ResponseEntity<UserInfo> bodyParamHeaderPath(@PathVariable("id") Long id, @RequestBody UserInfo userInfo) {
        userInfo.setId(id);
        userInfoService.updateById(userInfo);
        return ResponseEntity.ok(userInfo);
    }
}
