# Spring Boot 权限认证基础模板项目

## 📌 项目简介

本项目基于 **Spring Boot 3.3.4** 构建，集成了权限认证、JWT 登录、文件上传下载、接口文档、统一异常处理、操作日志记录、DTO/VO 转换等常用功能，采用模块化结构组织，适合作为后续业务开发的基础模板。

---

## 🛠️ 技术栈

| 技术 | 描述 |
|------|------|
| Spring Boot 3.3.4 | Java 应用开发框架 |
| Spring Security | 安全认证与权限控制 |
| JWT (jjwt) | Token 生成与解析 |
| MyBatis-Plus | ORM 框架，简化数据库操作 |
| MapStruct | 对象映射工具，用于 DTO <-> Entity |
| Knife4j | Swagger 增强版，生成在线接口文档 |
| Lombok | 简化实体类代码 |
| MySQL | 数据库 |
| Logback + SLF4J | 日志记录 |

---

## ✅ 功能列表

- [x] 登录认证（用户名 + 密码，JWT Token）
- [x] 权限控制（基于角色，支持注解如 `@PreAuthorize`）
- [x] 接口文档集成（Knife4j，自动生成）
- [x] DTO/VO/Entity 分离，使用 MapStruct 自动映射
- [x] 文件上传下载功能
- [x] 全局异常处理
- [x] 统一响应封装
- [x] 操作日志记录（支持异常打印等）
- [x] 模块化组织结构（controller、service、mapper、dto、vo 等）

---

## 🚀 快速启动

### 1. 克隆项目
```bash
git clone https://github.com/shiyuan598/demo-proj.git
cd demo-proj
```

### 2. 配置数据库连接
修改 `application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

### 3. 导入测试数据（可选）
项目根目录下 `static/sql` 中包含初始化 SQL 文件，可在本地 MySQL 中直接导入：

```bash
mysql -u root -p your_db < static/sql/init.sql
```

### 4. 启动项目
```bash
./mvnw spring-boot:run
```

### 5. 访问接口文档
启动成功后，控制台会输出接口文档地址，默认格式如下：

```
接口文档地址：http://localhost:9002/api/doc.html
```

---

## 🗂️ 项目结构说明（模块化）

```
src/main/java
└── com.example.project
    ├── common
    │   ├── config         # 配置类（Spring Security、Swagger 等）
    │   ├── exception      # 全局异常处理
    │   ├── response       # 通用返回体封装
    │   ├── security       # 权限相关工具类、用户上下文
    │   └── utils          # 工具类
    ├── modules
    │   ├── auth           # 登录鉴权模块
    │   ├── file           # 文件上传下载模块
    │   └── user           # 用户模块
    │       ├── controller
    │       ├── dto
    │       ├── entity
    │       ├── mapper
    │       ├── service
    │       └── vo
    └── Application.java

static/sql/
└── init.sql              # 初始化数据库测试数据
```

---

## 📝 使用说明

- 登录接口返回的 token 需在调用其他接口时放入请求头中：
```http
Authorization: Bearer <token>
```

- 权限注解使用示例：
```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/data")
public R<?> getAdminData() { ... }
```

---

## 📈 后续可扩展功能（建议）

- [ ] 操作日志记录增强（可考虑使用 AOP + 注解）
- [ ] 接入 EasyExcel，实现 Excel 导入导出
- [ ] 用户多角色支持、权限树结构管理
- [ ] 国际化、多语言支持

---

## 📮 联系方式

如有问题反馈，欢迎联系我：

📧 **wangyuanhpu@163.com**