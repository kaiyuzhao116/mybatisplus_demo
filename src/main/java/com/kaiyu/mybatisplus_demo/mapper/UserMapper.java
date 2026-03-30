package com.kaiyu.mybatisplus_demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaiyu.mybatisplus_demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
