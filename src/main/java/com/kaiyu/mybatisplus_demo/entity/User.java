package com.kaiyu.mybatisplus_demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data // Lombok注解，自动生成get/set/toString等方法
@TableName("user") // 对应数据库的表名
public class User {

    @TableId(type = IdType.ASSIGN_ID) // 主键，雪花算法生成
    private Long id;

    private String name;

    private Integer age;

    private String email;
}