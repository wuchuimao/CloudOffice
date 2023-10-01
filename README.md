# CloudOffice
* [一、项目介绍](#一项目介绍)<br>
* [二、搭建环境](#二搭建环境)<br>
    * [1.项目模块](#1项目模块)<br>
    * [2.配置依赖](#2配置依赖)<br>
    * [3.数据库创建](#3数据库创建)<br>
## 一、项目介绍
系统主要包括：管理端和员工端<br>

管理端包含：基于角色的权限管理，审批管理，公众号菜单管理。<br>
员工端采用微信公众号操作，包含：微信授权登入，消息推送等功能。<br>

项目服务器端架构：SpringBoot，MybatisPlus，SpringSecurity，Redis，Activiti，MySQL<br>
前端架构：vue-admin-template，Node.js， Npm，ElementUI，Axios<br>

## 二、搭建环境
###1.项目模块
cloud-office：根目录，管理子模块<br>
​    cmomon：公共类父模块<br>
​	common-util：核心工具类<br>
​	service-util：service模块工具类<br>
​	spring-security：spring-security业务模块<br>
​     model：实体类模块<br>
​     service-co：系统服务模块<br>
### 2.配置依赖
依赖配置查看文件 /查看文件/配置依赖.md
### 3.数据库创建
使用SQLyog执行 database/表结构.sql 文件，创建所需要的表。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/database.fig)<br>

