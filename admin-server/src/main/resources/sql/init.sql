CREATE DATABASE IF NOT EXISTS admin_db DEFAULT CHARACTER SET utf8mb4;
USE admin_db;

DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(50) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT NOT NULL DEFAULT 0,
    name VARCHAR(100) NOT NULL,
    path VARCHAR(200) NULL,
    permission VARCHAR(100) NULL,
    type TINYINT NOT NULL COMMENT '1菜单 2按钮',
    sort INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

INSERT INTO sys_user(username,password,nickname,status,create_time,update_time)
VALUES ('admin','21232f297a57a5a743894a0e4a801fc3','超级管理员',1,NOW(),NOW());

INSERT INTO sys_role(role_code,role_name,status,create_time,update_time)
VALUES ('ADMIN','管理员',1,NOW(),NOW());

INSERT INTO sys_menu(parent_id,name,path,permission,type,sort,status,create_time,update_time)
VALUES
(0,'用户管理','/users',NULL,1,1,1,NOW(),NOW()),
(0,'菜单管理','/menus',NULL,1,2,1,NOW(),NOW()),
(0,'角色管理','/roles',NULL,1,3,1,NOW(),NOW()),
(1,'新增用户',NULL,'user:add',2,1,1,NOW(),NOW()),
(1,'编辑用户',NULL,'user:edit',2,2,1,NOW(),NOW()),
(1,'删除用户',NULL,'user:delete',2,3,1,NOW(),NOW()),
(2,'新增菜单',NULL,'menu:add',2,1,1,NOW(),NOW()),
(2,'编辑菜单',NULL,'menu:edit',2,2,1,NOW(),NOW()),
(2,'删除菜单',NULL,'menu:delete',2,3,1,NOW(),NOW()),
(3,'新增角色',NULL,'role:add',2,1,1,NOW(),NOW()),
(3,'编辑角色',NULL,'role:edit',2,2,1,NOW(),NOW()),
(3,'删除角色',NULL,'role:delete',2,3,1,NOW(),NOW());

INSERT INTO sys_user_role(user_id,role_id) VALUES (1,1);
INSERT INTO sys_role_menu(role_id,menu_id)
SELECT 1,id FROM sys_menu;
