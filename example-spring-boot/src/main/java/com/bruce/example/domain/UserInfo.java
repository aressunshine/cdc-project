package com.bruce.example.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName user_info
 */
@TableName(value = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(name = "用户信息", description = "用户信息实体类")
public class UserInfo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Schema(title = "用户id", type = "long", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @Schema(title = "用户名", type = "string", example = "BruceZou")
    @TableField(value = "username")
    private String username;

    /**
     * 昵称
     */
    @Schema(title = "昵称", type = "string", example = "bruce")
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 年龄
     */
    @Schema(title = "年龄", type = "byte", example = "25")
    @TableField(value = "age")
    private Byte age;

    /**
     * 电话号码
     */
    @Schema(title = "电话号码", type = "string", example = "13800000000")
    @TableField(value = "telephone")
    private String telephone;

    /**
     * 邮箱
     */
    @Schema(title = "邮箱", type = "string", example = "123@bruce.com")
    @TableField(value = "email")
    private String email;

    /**
     * 性别，0：未知；1：男；2：女
     */
    @Schema(title = "性别，0：未知；1：男；2：女", type = "byte", example = "1")
    @TableField(value = "sex")
    private Byte sex;

    /**
     * 是否删除 ? 0 : 否; 1 : 是
     */
    @Schema(title = "是否删除 ? false : 否; true : 是", type = "boolean", example = "false")
    @TableField(value = "is_deleted")
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间", type = "dateTime", example = "2023-01-01 00:00:00")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间", type = "datetime", example = "2023-01-01 00:00:00")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}