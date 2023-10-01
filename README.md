# CloudOffice
[一、项目介绍](#一、项目介绍)<br>
[二、搭建环境](#二、搭建环境)<br>
>[1.项目模块](#1.项目模块)
>[2.配置依赖](#2.配置依赖)
## 一、项目介绍
系统主要包括：管理端和员工端<br>

管理端包含：基于角色的权限管理，审批管理，公众号菜单管理。<br>
员工端采用微信公众号操作，包含：微信授权登入，消息推送等功能。<br>

项目服务器端架构：SpringBoot，MybatisPlus，SpringSecurity，Redis，Activiti，MySQL<br>
前端架构：vue-admin-template，Node.js， Npm，ElementUI，Axios<br>

## 二、搭建环境
### 1.项目模块
cloud-office：根目录，管理子模块<br>
​		cmomon：公共类父模块<br>
​				common-util：核心工具类<br>
​				service-util：service模块工具类<br>
​				spring-security：spring-security业务模块<br>
​		model：实体类模块<br>
​		service-co：系统服务模块<br>
### 2.配置依赖
cloud-office中的pom.xml文件，管理依赖版本。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.6.RELEASE</version>
    </parent>

    <groupId>com.wu</groupId>
    <artifactId>cloud_office</artifactId>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>common</module>
        <module>model</module>
        <module>service-co</module>
    </modules>
    <packaging>pom</packaging>

    <properties>
        <java.version>1.8</java.version>
        <mybatis-plus.version>3.4.1</mybatis-plus.version>
        <mysql.version>8.0.30</mysql.version>
        <knife4j.version>3.0.3</knife4j.version>
        <jwt.version>0.9.1</jwt.version>
        <fastjson.version>2.0.21</fastjson.version>
        <druid.version>1.1.17</druid.version>
    </properties>

    <!--配置dependencyManagement锁定依赖的版本-->
    <dependencyManagement>
        <dependencies>
            <!--mybatis_plus-plus 持久层-->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>
            <!--mysql-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!--druid-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>${druid.version}</version>
            </dependency>
            <!--knife4j-->
            <dependency>
                <groupId>com.github.xiaoymin</groupId>
                <artifactId>knife4j-spring-boot-starter</artifactId>
                <version>${knife4j.version}</version>
            </dependency>
            <!--jjwt-->
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt</artifactId>
                <version>${jwt.version}</version>
            </dependency>
            <!--fastjson-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>${fastjson.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```
