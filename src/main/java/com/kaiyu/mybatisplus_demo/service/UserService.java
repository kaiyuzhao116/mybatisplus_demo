package com.kaiyu.mybatisplus_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaiyu.mybatisplus_demo.entity.User;

/**
 * 用户服务层接口
 * 继承MyBatis-Plus提供的IService，自带全套基础CRUD方法，无需自己写SQL
 */
public interface UserService extends IService<User> {

}