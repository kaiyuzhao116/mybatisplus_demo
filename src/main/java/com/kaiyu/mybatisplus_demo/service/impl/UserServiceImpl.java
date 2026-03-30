package com.kaiyu.mybatisplus_demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaiyu.mybatisplus_demo.entity.User;
import com.kaiyu.mybatisplus_demo.mapper.UserMapper;
import com.kaiyu.mybatisplus_demo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务层实现类
 * 继承ServiceImpl，自动注入Mapper，实现接口的所有CRUD方法
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}