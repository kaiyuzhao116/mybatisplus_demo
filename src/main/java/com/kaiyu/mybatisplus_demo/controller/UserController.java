package com.kaiyu.mybatisplus_demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaiyu.mybatisplus_demo.entity.User;
import com.kaiyu.mybatisplus_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * MyBatis-Plus学习用Controller
 * 基于RESTful风格设计，覆盖单查、全查、新增、修改、删除、条件查询、分页查询
 */
@RestController
@RequestMapping("/user")
public class UserController {

    // 注入用户服务
    @Autowired
    private UserService userService;

    /**
     * 1. 根据ID查询单个用户
     * 请求方式：GET
     * 访问示例：http://localhost:8080/user/1
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        // MyBatis-Plus自带根据ID查询方法
        return userService.getById(id);
    }

    /**
     * 2. 查询所有用户
     * 请求方式：GET
     * 访问示例：http://localhost:8080/user/list
     */
    @GetMapping("/list")
    public List<User> getAllUsers() {
        // MyBatis-Plus自带全量查询方法
        return userService.list();
    }

    /**
     * 3. 新增用户
     * 请求方式：POST
     * 访问地址：http://localhost:8080/user
     * 请求体（JSON格式，id无需传，雪花算法自动生成）：
     * {
     *     "name": "测试用户",
     *     "age": 22,
     *     "email": "test@kaiyu.com"
     * }
     */
    @PostMapping
    public String addUser(@RequestBody User user) {
        boolean isSuccess = userService.save(user);
        return isSuccess ? "新增成功！用户ID：" + user.getId() : "新增失败！";
    }

    /**
     * 4. 根据ID修改用户信息
     * 请求方式：PUT
     * 访问地址：http://localhost:8080/user
     * 请求体（JSON格式，必须传id，根据id更新）：
     * {
     *     "id": 1,
     *     "name": "修改后的姓名",
     *     "age": 25,
     *     "email": "update@kaiyu.com"
     * }
     */
    @PutMapping
    public String updateUser(@RequestBody User user) {
        boolean isSuccess = userService.updateById(user);
        return isSuccess ? "修改用户成功！" : "修改用户失败！";
    }

    /**
     * 5. 根据ID删除用户
     * 请求方式：DELETE
     * 访问示例：http://localhost:8080/user/1
     */
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {
        boolean isSuccess = userService.removeById(id);
        return isSuccess ? "删除用户成功！" : "删除用户失败！";
    }

    /**
     * 6. 条件查询（模糊查询+范围查询）
     * 需求：姓名模糊匹配、年龄区间筛选
     * 请求方式：GET
     * 访问示例：http://localhost:8080/user/condition?name=J&minAge=18&maxAge=25
     */
    @GetMapping("/condition")
    public List<User> getUserByCondition(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge
    ) {
        // Lambda条件构造器：避免硬编码字段名，编译期就能校验错误，学习首选
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 姓名模糊查询：name不为空时，才拼接该条件
        queryWrapper.like(name != null, User::getName, name);
        // 年龄 >= 最小值
        queryWrapper.ge(minAge != null, User::getAge, minAge);
        // 年龄 <= 最大值
        queryWrapper.le(maxAge != null, User::getAge, maxAge);
        // 执行条件查询
        return userService.list(queryWrapper);
    }

    /**
     * 7. 分页查询用户
     * 请求方式：GET
     * 访问示例：http://localhost:8080/user/page?pageNum=1&pageSize=2
     * pageNum：当前页码，默认1；pageSize：每页条数，默认10
     */
    @GetMapping("/page")
    public Page<User> getUserByPage(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize
    ) {
        // 构建分页对象
        Page<User> pageParam = new Page<>(pageNum, pageSize);
        // 执行分页查询，第二个参数可传入条件构造器做带条件的分页
        return userService.page(pageParam);
    }
    // ==================== 【🔥 进阶功能 工作必用】 ====================

    /**
     * 1. 批量新增用户
     */
    @PostMapping("/batch")
    public String saveBatch(@RequestBody List<User> userList) {
        return userService.saveBatch(userList) ? "批量新增成功" : "失败";
    }

    /**
     * 2. 批量删除（根据ID集合）
     * 测试链接：/user/batch?ids=1,2,3
     */
    @DeleteMapping("/batch")
    public String deleteBatch(@RequestParam List<Long> ids) {
        return userService.removeByIds(ids) ? "批量删除成功" : "失败";
    }

    /**
     * 3. 只查询指定字段（不查全部，提高性能）
     * 例：只查 name 和 age
     */
    @GetMapping("/select/columns")
    public List<User> selectColumns() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getName, User::getAge); // 只查这两列
        return userService.list(wrapper);
    }

    /**
     * 4. 排序查询（按年龄倒序）
     */
    @GetMapping("/order/age")
    public List<User> orderByAge() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getAge); // 倒序
        return userService.list(wrapper);
    }

    /**
     * 5. 统计总数量
     */
    @GetMapping("/count")
    public Long count() {
        return userService.count();
    }

    /**
     * 6. 条件统计：年龄大于20的人数
     */
    @GetMapping("/count/age")
    public Long countByAge(@RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(User::getAge, age);
        return userService.count(wrapper);
    }

    /**
     * 7. 动态更新：只更新非空字段（不会覆盖null值）
     * 例：只改邮箱，不改姓名年龄
     */
    @PatchMapping("/dynamic")
    public String dynamicUpdate(@RequestBody User user) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, user.getId());
        wrapper.set(user.getEmail() != null, User::getEmail, user.getEmail());
        wrapper.set(user.getName() != null, User::getName, user.getName());
        return userService.update(wrapper) ? "动态更新成功" : "失败";
    }

    /**
     * 8. 查询返回 Map 格式（不需要实体类时用）
     */
    @GetMapping("/map")
    public List<Map<String, Object>> selectMap() {
        return userService.listMaps();
    }

    /**
     * 9. 精确匹配查询（姓名=xxx 且 年龄=xxx）
     */
    @GetMapping("/eq")
    public List<User> eq(
            @RequestParam String name,
            @RequestParam Integer age
    ) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, name);
        wrapper.eq(User::getAge, age);
        return userService.list(wrapper);
    }

    /**
     * 10. 分页 + 条件 组合查询（最常用！）
     */
    @GetMapping("/page/condition")
    public Page<User> pageAndCondition(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "5") Long pageSize,
            @RequestParam(required = false) String name
    ) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, User::getName, name);
        return userService.page(new Page<>(pageNum, pageSize), wrapper);
    }
}