package com.kaiyu.mybatisplus_demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaiyu.mybatisplus_demo.entity.User;
import com.kaiyu.mybatisplus_demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyBatisPlusDemoApplicationTests {
    // 注入Mapper，直接使用
    @Autowired
    private UserMapper userMapper;
    // 1. 根据ID查询
    @Test
    void testSelectById() {
        User user = userMapper.selectById(1);
        System.out.println("查询结果：" + user);
    }

    // 2. 查询所有数据
    @Test
    void testSelectAll() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    // 3. 新增数据
    @Test
    void testInsert() {
        User user = new User();
        user.setName("黑马程序员");
        user.setAge(22);
        user.setEmail("heima@example.com");
        // 插入成功后，雪花算法生成的ID会自动回写到user对象中
        int rows = userMapper.insert(user);
        System.out.println("影响行数：" + rows);
        System.out.println("生成的主键ID：" + user.getId());
    }

    // 4. 根据ID更新
    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(1L);
        user.setAge(19); // 只更新age字段
        int rows = userMapper.updateById(user);
        System.out.println("影响行数：" + rows);
    }

    // 5. 根据ID删除
    @Test
    void testDeleteById() {
        int rows = userMapper.deleteById(5L);
        System.out.println("影响行数：" + rows);
    }

    // 6. 条件查询（查询年龄大于20的用户）
    @Test
    void testSelectByCondition() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 构建条件：age > 20
        queryWrapper.gt(User::getAge, 20);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    // 7. 分页查询（需额外配置分页插件，见下方补充）
    @Test
    void testSelectByPage() {
        // 第1页，每页2条数据
        Page<User> page = new Page<>(1, 2);
        Page<User> userPage = userMapper.selectPage(page, null);
        System.out.println("总条数：" + userPage.getTotal());
        System.out.println("总页数：" + userPage.getPages());
        System.out.println("当前页数据：");
        userPage.getRecords().forEach(System.out::println);
    }

}
