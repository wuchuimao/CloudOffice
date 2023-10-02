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
        * [4.1JWT生成token和解析token](#41JWT生成token和解析token)
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
菜单管理的相关操作在security-util模块中的com.wu.security包下和service-co模块中的com.wu.auth.controller包下的IndexController.java中<br>
本项目需要实现的权限分为菜单权限和按钮权限<br>
**菜单权限**<br>
菜单权限就是对页面的控制，就是有这个权限的用户才能访问这个页面，没这个权限的用户就无法访问，它是以整个页面为维度，对权限的控制并没有那么细，所以是一种**粗颗粒权限**。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/menu-permissions.jpg)<br>
**按钮权限**<br>
按钮权限就是将页面的**操作**视为资源，比如删除操作，有些人可以操作有些人不能操作。对于后端来说，操作就是一个接口。于前端来说，操作往往是一个按钮，是一种**细颗粒权限**。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/button-permission.jpg)<br>
通过实现两个接口来实现权限管理：<br>
1.用户登入，根据用户名和密码判断是否存在该用户，存在则通过用户id和用户名生成对应的token<br>
2.登入成功，根据token获取用户相关信息（菜单权限以及按钮权限等）<br>
#### 4.1JWT生成token和解析token
JWT是JSON Web Token的缩写，即JSON Web令牌，是一种自包含令牌。 是为了在网络应用环境间传递声明而执行的一种基于JSON的开放标准。
JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息，以便于从资源服务器获取资源。比如用在用户登录上。
JWT最重要的作用就是对 token信息的防伪作用。<br>
一个JWT由三个部分组成：**JWT头、有效载荷、签名哈希**，最后由这三者组合进行base64url编码得到JWTtoken，典型的，一个JWT看起来如下图：该对象为一个很长的字符串，字符之间通过"."分隔符分为三个子串。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/jwt.jpg)<br>
**JWT头**<br>
JWT头部分是一个描述JWT元数据的JSON对象，通常如下所示。<br>
```
{
  "alg": "HS256",
  "typ": "JWT"
}
```
在上面的代码中，alg属性表示签名使用的算法，默认为HMAC SHA256（写为HS256）；typ属性表示令牌的类型，JWT令牌统一写为JWT。最后，使用Base64 URL算法将上述JSON对象转换为字符串保存。<br>
**有效载荷**<br>
有效载荷部分，是JWT的主体内容部分，也是一个JSON对象，包含需要传递的数据。 JWT指定七个默认字段供选择。<br>
```
iss: jwt签发者
sub: 主题
aud: 接收jwt的一方
exp: jwt的过期时间，这个过期时间必须要大于签发时间
nbf: 定义在什么时间之前，该jwt都是不可用的.
iat: jwt的签发时间
jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
```
主要设置主题部分<br>
```
{
  "name": "Helen",
  "role": "editor",
  "avatar": "helen.jpg"
}
```
请注意，默认情况下JWT是未加密的，任何人都可以解读其内容，因此不要构建隐私信息字段，存放保密信息，以防止信息泄露。JSON对象也使用Base64 URL算法转换为字符串保存。<br>
**签名哈希**<br>
签名哈希部分是对上面两部分数据签名，通过指定的算法生成哈希，以确保数据不会被篡改。<br>
首先，需要指定一个密码（secret）。该密码仅仅为保存在服务器中，并且不能向用户公开。然后，使用标头中指定的签名算法（默认情况下为HMAC SHA256）根据以下公式生成签名。<br>
```
HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(claims), secret)    ==>   签名hash
```
在计算出签名哈希后，JWT头，有效载荷和签名哈希的三个部分组合成一个字符串，每个部分用"."分隔，就构成整个JWT对象。<br>
**Base64URL算法**<br>
如前所述，JWT头和有效载荷序列化的算法都用到了Base64URL。该算法和常见Base64算法类似，稍有差别。<br>
作为令牌的JWT可以放在URL中（例如api.example/?token=xxx）。 Base64中用的三个字符是"+"，"/"和"="，由于在URL中有特殊含义，因此Base64URL中对他们做了替换："="去掉，"+"用"-"替换，"/"用"_"替换，这就是Base64URL算法。<br>

项目中在common-util模块中集成JWT<br>
引入依赖<br>
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
</dependency>
```
在common-util模块中的com.wu.common.jwt包下的JwtHelper.java中实现生成token和解析token<br>
