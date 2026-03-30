# MyBatis-Plus Demo
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.7-blue)
![License](https://img.shields.io/badge/License-MIT-green)

这是一个基于 Spring Boot 3.x + MyBatis-Plus 3.5.7 的学习示例项目，包含基础 CRUD、条件查询、分页查询、批量操作等核心功能。

## 项目结构
```
com.kaiyu.mybatisplus_demo
├── config/          # MyBatis-Plus 配置类（分页插件等）
├── controller/      # 控制层（REST接口）
├── entity/          # 实体类
├── mapper/          # 数据访问层（继承BaseMapper）
├── service/         # 业务层接口
└── service/impl/    # 业务层实现类
```

## 核心功能
- 基础 CRUD（增删改查）
- 条件查询（模糊匹配、范围筛选）
- 分页查询
- 批量新增/删除
- 动态更新（只更新非空字段）
- 统计查询、排序查询
- 分页+条件组合查询（企业最常用）

---

## 数据库建表 SQL

```sql
-- 用户表
CREATE TABLE `user`
(
    `id`    BIGINT AUTO_INCREMENT COMMENT '主键ID',
    `name`  VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
    `age`   INT                                                         DEFAULT NULL COMMENT '年龄',
    `email` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户信息表';

-- 初始化测试数据
INSERT INTO `user` (`name`, `age`, `email`)
VALUES ('Jone', 18, 'test1@baomidou.com'),
       ('Jack', 20, 'test2@baomidou.com'),
       ('Tom', 28, 'test3@baomidou.com'),
       ('Sandy', 21, 'test4@baomidou.com'),
       ('Billie', 24, 'test5@baomidou.com');
```

---

## 接口测试清单

### 1. 基础 CRUD 接口
| 功能 | 请求方式 | 测试链接 | 说明 |
|------|----------|----------|------|
| 根据ID查询单个用户 | GET | `http://localhost:8080/user/{id}` | 替换{id}为实际ID，如`/user/1` |
| 查询所有用户 | GET | `http://localhost:8080/user/list` | 返回表中所有数据 |
| 新增用户 | POST | `http://localhost:8080/user` | 请求体JSON：`{"name":"测试用户","age":22,"email":"test@kaiyu.com"}` |
| 修改用户 | PUT | `http://localhost:8080/user` | 请求体JSON：`{"id":1,"name":"修改后姓名","age":25,"email":"update@kaiyu.com"}` |
| 删除用户 | DELETE | `http://localhost:8080/user/{id}` | 替换{id}为要删除的用户ID |

---

### 2. 条件查询接口
| 功能 | 请求方式 | 测试链接 | 说明 |
|------|----------|----------|------|
| 模糊+范围条件查询 | GET | `http://localhost:8080/user/condition?name=J&minAge=18&maxAge=25` | name：姓名模糊匹配；minAge：最小年龄；maxAge：最大年龄 |
| 精确匹配查询 | GET | `http://localhost:8080/user/eq?name=Jack&age=20` | 姓名和年龄完全匹配 |
| 排序查询（按年龄倒序） | GET | `http://localhost:8080/user/order/age` | 按年龄从大到小排序 |
| 只查询指定字段 | GET | `http://localhost:8080/user/select/columns` | 只返回name和age字段 |

---

### 3. 分页查询接口
| 功能 | 请求方式 | 测试链接 | 说明 |
|------|----------|----------|------|
| 基础分页 | GET | `http://localhost:8080/user/page?pageNum=1&pageSize=2` | pageNum：页码；pageSize：每页条数 |
| 分页+条件组合 | GET | `http://localhost:8080/user/page/condition?name=J&pageNum=1&pageSize=2` | 模糊匹配+分页 |

---

### 4. 批量/统计接口
| 功能 | 请求方式 | 测试链接 | 说明 |
|------|----------|----------|------|
| 批量新增用户 | POST | `http://localhost:8080/user/batch` | 请求体为用户数组JSON |
| 批量删除用户 | DELETE | `http://localhost:8080/user/batch?ids=1,2,3` | 传入多个ID用逗号分隔 |
| 查询总人数 | GET | `http://localhost:8080/user/count` | 返回用户总数 |
| 条件统计 | GET | `http://localhost:8080/user/count/age?age=20` | 统计年龄大于20的用户数 |
| 返回Map格式 | GET | `http://localhost:8080/user/map` | 以Map格式返回数据 |

---

### 5. 动态更新接口
| 功能 | 请求方式 | 测试链接 | 说明 |
|------|----------|----------|------|
| 动态更新（只改非空字段） | PATCH | `http://localhost:8080/user/dynamic` | 请求体JSON：`{"id":1,"email":"new@kaiyu.com"}`，只更新邮箱 |

---

## 配置说明

### application.yml 关键配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mp_demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 开启SQL日志
```

### MyBatis-Plus 分页配置
```java
package com.kaiyu.mybatisplus_demo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

---

## 快速开始
1. 克隆项目：`git clone https://github.com/kaiyuzhao116/mybatisplus_demo.git`
2. 执行数据库建表SQL，初始化测试数据
3. 修改 `application.yml` 中的数据库账号密码
4. 启动 `MyBatisPlusDemoApplication` 类
5. 访问 `http://localhost:8080/user/list` 验证接口

---




