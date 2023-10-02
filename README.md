# CloudOffice
* [一、项目介绍](#一项目介绍)<br>
* [二、搭建环境](#二搭建环境)<br>
    * [1.项目模块](#1项目模块)<br>
    * [2.配置依赖](#2配置依赖)<br>
    * [3.数据库创建](#3数据库创建)<br>
* [三、前端界面](#三前端界面)
* [四、后端功能实现](#四后端功能实现)
    * [1.用户管理](#1用户管理)
    * [2.角色管理](#2角色管理)
    * [3.菜单管理](#3菜单管理)
    * [4.权限管理](#4权限管理)
## 一、项目介绍
系统主要包括：管理端和员工端<br>

管理端包含：基于角色的权限管理，审批管理，公众号菜单管理。<br>
员工端采用微信公众号操作，包含：微信授权登入，消息推送等功能。<br>

项目服务器端架构：SpringBoot，MybatisPlus，SpringSecurity，Redis，Activiti，MySQL<br>
前端架构：vue-admin-template，Node.js， Npm，ElementUI，Axios<br>
## 二、搭建环境
### 1.项目模块
* cloud_office：根目录，管理子模块<br>
    * cmomon：公共类父模块<br>
        * common-util：核心工具类<br>
        * service-util：service模块工具类<br>
        * spring-security：spring-security业务模块<br>
    * model：实体类模块<br>
    * service-co：系统服务模块<br>
### 2.配置依赖
依赖配置查看文件 /参考文件/配置依赖.md
### 3.数据库创建
使用SQLyog执行 /database/表结构.sql 文件，创建所需要的表。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/database.jpg)<br>
## 三、前端界面
前端界面在 cloud_office_front中。<br>
管理端页面项目为wu-auth-ui，在VS Code中使用命令行npm run dev运行该项目；<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/ui.jpg)<br>
管理端登入界面<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/login.jpg)<br>
管理端界面<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/front.jpg)<br>
前端审批页面为项目为wu-co-web，在VS Code中使用命令行npm run server运行该项目；
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/web.jpg)<br>
前端审批界面
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approve.jpg)<br>
前端界面具体配置查看文件 /参考文件/前端知识.md，/参考文件/角色管理前端界面.md。<br>
## 四、后端功能实现
### 1.用户管理
用户管理的相关操作在service-co模块中的com.wu.auth.controller包下的SysUserController.java中<br>
包含了用户的条件分页查询，用户的添加，修改，删除以及给用户分配角色。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/user.jpg)<br>
给用户分配角色，是先查询角色表中的所有角色，然后根据用户id在用户-角色的表中查询用户所具有的角色id，再通过角色id获取用户拥有的角色并封装在map中。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/user-controller.jpg)<br>
### 2.角色管理
菜单管理的相关操作在service-co模块中的com.wu.auth.controller包下的SysRoleController.java中<br>
和用户管理类似，包含条件分页查询，角色的添加，修改，删除，以及给角色分配菜单。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/role.jpg)<br>
在给角色分配菜单权限时，需要将从数据库中查询的菜单构建成前端显示的树形结构。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/role-tree.jpg)<br>
### 3.菜单管理
菜单管理的相关操作在service-co模块中的com.wu.auth.controller包下的SysMenuController.java中<br>
包含了菜单的增删改查，其中从数据库中查询的菜单列表需要转换为前端显示的树形结构；删除菜单时需要判断有无下一级菜单，只有没有下一级菜单或者按钮时才能删除。
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/menu.jpg)<br>
### 4.权限管理
