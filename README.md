# Admin 管理系统（前后端分离）

包含两个项目：

- `admin-server`：JDK 17 + Spring Boot 3.3.5 + MyBatis-Plus 3.5.5 + MySQL
- `admin`：React + Ant Design

## 1. 后端 admin-server

### 初始化数据库
1. 创建 MySQL 数据库并执行脚本：`admin-server/src/main/resources/sql/init.sql`
2. 修改 `admin-server/src/main/resources/application.yml` 中数据库连接

### 启动
```bash
cd admin-server
mvn spring-boot:run
```

默认端口：`8080`

默认管理员：
- 用户名：`admin`
- 密码：`admin`

## 2. 前端 admin

### 启动
```bash
cd admin
npm install
npm run dev
```

默认端口：`5173`

## 功能说明

### 登录与权限
- 登录时后端先查用户角色，再查角色拥有的菜单和按钮权限。
- 前端根据返回的菜单动态渲染侧边栏，根据按钮权限控制“新增/编辑/删除”按钮显示状态。

### 管理模块
- 用户管理：用户 CRUD，分配角色。
- 菜单管理：菜单/按钮权限 CRUD。
- 角色管理：角色 CRUD，分配菜单和按钮权限。
