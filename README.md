
# Spring Boot 基础模板项目 

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
- [x] 操作日志记录
- [x] 模块化组织结构（controller、service、mapper、dto、vo 等）

---

## 🚀 快速启动

### 1. 克隆项目
```bash
git clone https://github.com/shiyuan598/demo-proj.git
cd demo-proj
```

### 2. 配置数据库连接
修改 `application-*.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### 3. 导入测试数据（可选）
项目根目录下 `resources/static` 中包含初始化 SQL 文件，可在本地 MySQL 中直接导入：

```bash
mysql -u root -p your_db < static/init.sql
```

### 4. 生成 JWT 密钥（推荐）
```bash
openssl rand -base64 64
```
复制生成结果，作为环境变量中的 JWT 密钥。

### 5. 设置环境变量（Linux/macOS）
```bash
export SPRING_PROFILES_ACTIVE=dev
export JWT_SECRET=your_secure_secret_key_base64
export JWT_EXPIRATION=86400000
```

> ⚠️ 建议生产环境中通过环境变量注入 JWT 密钥，避免写死在配置文件中。

### 5. 启动项目
```bash
./mvnw spring-boot:run
```

### 6. 访问接口文档
启动成功后，控制台会输出接口文档地址，默认格式如下：

```
接口文档地址：http://localhost:9002/api/doc.html
```

---

## 🚢 部署说明
### 1. 🔧 配置环境（dev / prod 区分）
已在 `application.yml` 中支持多环境配置，通过命令行参数或环境变量控制：
* `application-dev.yml`：开发环境配置（默认使用本地数据库）
* `application-prod.yml`：生产环境配置（用于部署环境）

✅ 默认入口配置文件为 `application.yml`，其中引用了 `${spring.profiles.active}` 来动态加载不同配置

### 2. ☕ 方式一：使用 Jar 包部署（传统方式）
```bash
# 打包
./mvnw clean package -DskipTests

# 设置环境变量（或使用 -D 参数）
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET=your_secure_secret_key_base64
export JWT_EXPIRATION=86400000

# 启动项目
java -jar target/demo-proj-0.0.1-SNAPSHOT.jar
```
3. 访问服务

- 默认端口为 9002，接口地址为：
```
http://localhost:9002/api
```

### 3.🐳 方式二：使用 Docker 部署
**Dockerfile 示例**
```dockerfile
FROM eclipse-temurin:23-jdk-jammy

WORKDIR /app
COPY . /app
RUN ./mvnw clean package -DskipTests
EXPOSE 9002

CMD ["java", "-jar", "target/demo-proj-0.0.1-SNAPSHOT.jar"]
```

**构建镜像**
```bash
docker build -t demo-proj:latest .
```

**启动容器（推荐方式：使用环境变量）**
```bash
docker run -d -e "SPRING_PROFILES_ACTIVE=prod" -e "JWT_SECRET=你的密钥" -e "JWT_EXPIRATION=86400000" -p 9002:9002 --name demo-proj demo-proj:latest
```

📝 环境变量说明

| 变量名            | 说明                     |
|------------------|------------------------|
| SPRING_PROFILES_ACTIVE | 启动配置环境，如：dev、prod      |
| JWT_SECRET        | JWT 加密密钥，用于签发和校验 Token |
| JWT_EXPIRATION | JWT 过期时间     |

---

## 🗂️ 项目结构说明（模块化）

```
demo-proj
    src/main
    ├── java
    │    └── com.shiyuan.base
    │        ├── common
    │        │   ├── config         # 配置类（Spring Security、MybatisPlus、Swagger 等）
    │        │   ├── exception      # 全局异常处理
    │        │   ├── log            # 日志
    │        │   ├── response       # 通用返回体封装
    │        │   ├── security       # 权限相关工具类、用户上下文
    │        │   └── utils          # 工具类
    │        ├── modules
    │        │   ├── auth           # 登录鉴权模块
    │        │   ├── file           # 文件上传下载模块
    │        │   └── user           # 用户模块
    │        │       ├── controller
    │        │       ├── dto
    │        │       ├── entity
    │        │       ├── mapper
    │        │       ├── service
    │        │       └── vo
    │        └── Application.java
    └── resources
        ├── mapper
        ├── static/init.sql          # 初始化数据库测试数据
        ├── application.yml
        ├── application-dev.yml
        ├── application-prod.yml
        └── logback-spring.xml
    Dockerfile
    pom.xml
    README.md
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
- [ ] 定时任务、WebSocket、Redis 缓存、限流、灰度发布……
- [ ] 集成 Flyway版本化数据库变更
- [ ] 接入 EasyExcel，实现 Excel 导入导出
- [ ] 用户多角色支持、权限树结构管理
- [ ] 国际化、多语言支持

---

## 📮 联系方式

如有问题反馈，欢迎联系我：

📧 **wangyuanhpu@163.com**
