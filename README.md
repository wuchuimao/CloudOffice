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
        * [4.2用户登入](#42用户登入)
        * [4.3获取用户信息](#43获取用户信息)
        * [4.4SpringSecurity](#44SpringSecurit)
    * [5.审批设置](#5审批设置)
    * [6.审批管理](#6审批管理)
    * [7.审批流程的申请和处理](#7审批流程的申请和处理)
    * [8.整合微信公众号](#8整合微信公众号)
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
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/ui.gif)<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval.gif)<br>
前端界面在 cloud_office_front中。<br>
管理端页面项目为wu-auth-ui，在VS Code中使用命令行npm run dev运行该项目(运行前要安装Node.js,然后安装依赖 npm install)；<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/ui.jpg)<br>
管理端登入界面<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/login-front.jpg)<br>
管理端界面<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/front.jpg)<br>
（员工端）前端审批页面为项目为wu-co-web，在VS Code中使用命令行npm run server运行该项目(运行前要安装Node.js,然后安装依赖 npm install)；
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/web.jpg)<br>
（员工端）前端审批界面
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval-center.jpg)<br>
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
在给角色分配菜单权限时，需要将从数据库中查询的菜单构建成前端显示的树形结构（根据menu表中的parent_id判断层级,根据封装menu数据的SysMenu类型中的isSelect判断是否显示选中该权限）。<br>
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
#### 4.2用户登入
代码在service-co模块中的com.wu.auth.controller包下的IndexController.java中<br>
通过用户名查询用户，判断用户是否存在和对比用户密码和登入密码是否一致，用户存在且密码一致则通过JWT根据用户id和用户名生成token，并将token返回，浏览器会将token放置与请求头中。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/login.jpg)<br>
数据库中的用户密码是用MD5加密过的，所以对比过程中登入密码也要进行MD5加密后在对比；Result类型是设计为返回json数据进行封装使用的。<br>
#### 4.3获取用户信息
代码在service-co模块中的com.wu.auth.controller包下的IndexController.java中<br>
登入成功后，接受浏览器发来的token，并通过JWT进行解析，获取用户名，再通过用户名获取对应user，然后获取该user对应的角色，在通过角色获取对应的菜单权限和按钮权限，将信息存在map中并最后封装为Result类型返回。<br>
**通过用户名最终获取菜单权限和按钮权限，是通过三表联合查询，分别为user表，role表，menu表，三张表的联合查询是通过两张中间表进行连接，分别为sys_role_menu，sys_user_role**<br>
在controller中<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/info.jpg)<br>
在service中<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/info-service.jpg)<br>
**根据用户权限查询的菜单权限和按钮权限构建前端对应的路由界面**<br>
admin用户显示的的界面（所有权限都有）<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/admin-permission.jpg)<br>
wjl用户显示的界面（只有用户管理和菜单管理的部分权限）
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/wjl-permission.jpg)<br>
#### 4.4SpringSecurity<br>
未使用SpringSecurity时访问存在问题<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/IndexController.jpg)<br>
使用SpringSecurity后，解决该问题。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/SpringSecurity.jpg)<br>
Spring Security 基于 Spring 框架，提供了一套 Web 应用安全性的完整解决方案， SpringSecurity 重要核心功能为用户认证指和用户授权。<br>
用户认证指的是：验证某个用户是否为系统中的合法主体，也就是说用户能否访问该系统。用户认证一般要求用户提供用户名和密码，系统通过校验用户名和密码来完成认证过程。<br>
**通俗点说就是系统认为用户是否能登录**<br>
用户授权指的是验证某个用户是否有权限执行某个操作。在一个系统中，不同用户所具有的权限是不同的。比如对一个文件来说，有的用户只能进行读取，而有的用户可以进行修改。一般来说，系统会为不同的用户分配不同的角色，而每个角色则对应一系列的权限。<br>
**通俗点讲就是系统判断用户是否有权限去做某些事情**<br>
Spring Security进行认证和授权的时候,就是利用的一系列的Filter来进行拦截的。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/security-filter.jpg)<br>
如图所示，一个请求想要访问到API就会从左到右经过蓝线框里的过滤器，其中**绿色部分是负责认证的过滤器，蓝色部分是负责异常处理，橙色部分则是负责授权**。<br>
这里面我们只需要重点关注两个过滤器即可：`UsernamePasswordAuthenticationFilter`负责登录认证，`FilterSecurityInterceptor`负责权限授权。<br>
在security-util模块中配置SpringSecurity。<br>
添加依赖<br>
```xml
  <!-- Spring Security依赖 -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
```
依赖包（spring-boot-starter-security）导入后，Spring Security就默认提供了许多功能将整个应用给保护了起来：<br>
​	1、要求经过身份验证的用户才能与应用程序进行交互<br>
​	2、创建好了默认登录表单<br>
​	3、生成用户名为`user`的随机密码并打印在控制台上<br>
​	等等......<br>
添加配置类
```Java
@Configuration
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的默认行为
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
}
```
**4.4.1用户认证**<br>
用户认证流程图：<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/user-permission.jpg)<br>
**Spring Security中三个核心组件**<br>
​	1、`Authentication`：存储了认证信息，代表当前登录用户<br>
​	2、`SeucirtyContext`：上下文对象，用来获取`Authentication`<br>
​	3、`SecurityContextHolder`：上下文管理对象，用来在程序任何地方获取`SecurityContext`<br>
**`Authentication`中的信息**<br>
​	1、`Principal`：用户信息，没有认证时一般是用户名，认证后一般是用户对象<br>
​	2、`Credentials`：用户凭证，一般是密码<br>
​	3、`Authorities`：用户权限<br>
我们需要通过 **`SecurityContext`** 来获取`Authentication`，`SecurityContext`就是我们的上下文对象！这个上下文对象则是交由 **`SecurityContextHolder`** 进行管理，你可以在程序**任何地方**使用它：<br>
```
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
```
`SecurityContextHolder`原理非常简单，就是使用`ThreadLocal`来保证一个线程中传递同一个对象！<br>

`AuthenticationManager` 就是Spring Security用于执行身份验证的组件，只需要调用它的`authenticate`方法即可完成认证。<br>
`AuthenticationManager`的校验逻辑<br>
   * 1、**`UserDetialsService`**根据用户名查询出用户对象，该接口只有一个方法`loadUserByUsername(String username)`，通过用户名查询用户对象，默认实现是在内存中查询。<br>
   * 2、查询出来的用户对象是由**`UserDetails`**来封装，该接口中提供了账号、密码等通用属性。<br>
   * 3、**PasswordEncoder**组件对密码进行校验。<br>
这里我们自定义实现UserDetialsService，UserDetails，PasswordEncoder三个组件，分别为UserDetailsServiceImpl，CustomUser，CustomMd5PasswordEncoder，存放于security-util模块中的com.wu.security.custom包下。<br>
UserDetailsServiceImpl实现UserDetialsService接口，<br>
CustomUser继承org.springframework.security.core.userdetails.User，<br>
CustomMd5PasswordEncoder实现PasswordEncoder接口<br>

**自定义用户认证接口TokenLoginFilter继承UsernamePasswordAuthenticationFilter<br>**
```Java
package com.wu.security.fillter;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wu.common.jwt.JwtHelper;
import com.wu.common.result.Result;
import com.wu.common.result.ResultCodeEnum;
import com.wu.common.util.ResponseUtil;
import com.wu.security.custom.CustomUser;
import com.wu.vo.system.LoginVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-04-08 19:21
 * @ Description：
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
        this.redisTemplate = redisTemplate;
    }

    /**
     * 登录认证
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            //获取用户信息
            LoginVo loginVo = new ObjectMapper().readValue(req.getInputStream(), LoginVo.class);
            //封装对象
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            //认证并返回,
            // authenticate()里面会通过authenticationToken中的userName查询数据库中对应的信息(包括权限)，并将信息封装再customUser中，
            // 然后CustomMd5PasswordEncoder对密码进行校验,校验完将user信息封装在Authentication中，然后进行下一步的操作。
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 登录成功
     * @param request
     * @param response
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        //获取当前用户
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        //生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());
        //保存权限数据
        redisTemplate.opsForValue().set(customUser.getUsername(), JSON.toJSONString(customUser.getAuthorities()));
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    /**
     * 登录失败
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {
        ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
    }
}
```
**自定义解析token类TokenAuthenticationFilter，继承于 OncePerRequestFilter**
```Java
package com.wu.security.fillter;

import com.alibaba.fastjson.JSON;
import com.wu.common.config.exception.WuException;
import com.wu.common.jwt.JwtHelper;
import com.wu.common.result.Result;
import com.wu.common.result.ResultCodeEnum;
import com.wu.common.util.ResponseUtil;
import com.wu.security.custom.LoginUserInfoHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-04-08 20:11
 * @ Description：用来判断是否已经登入,是登入路径则到TokenLoginFilter校验，不是登入路径则判断是否有token，有token再通过token获取用户名进而获取菜单权限和按钮权限，无则无法获取信息
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_AUTH));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            logger.info("useruame:"+username);
            if (!StringUtils.isEmpty(username)) {
                //通过ThreadLocal记录当前登录人信息
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
                LoginUserInfoHelper.setUsername(username);

                //通过userName从redis中获取权限数据
                String authoritiesString = (String) redisTemplate.opsForValue().get(username);
                //把redis中的字符串权限数据转换为要求的集合类型List<SimpleGrantedAuthority>
                List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                if(CollectionUtils.isEmpty(mapList)){
                    throw new WuException(ResultCodeEnum.LOGIN_AUTH);
                }
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map : mapList) {
                    authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
                }
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }else {
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            }
        }
        return null;
    }
}
```
**修改WebSecurityConfig配置类**
```Java
package com.wu.security.config;

import com.wu.security.custom.CustomMd5PasswordEncoder;
import com.wu.security.custom.UserDetailsService;
import com.wu.security.fillter.TokenAuthenticationFilter;
import com.wu.security.fillter.TokenLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-04-08 17:40
 * @ Description：
 */
@Configuration
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启基于方法的安全认证机制，也就是说在web层的controller启用注解机制的安全确认
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomMd5PasswordEncoder customMd5PasswordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 这是配置的关键，决定哪些接口开启防护，哪些接口绕过防护
        http
                //关闭csrf跨站请求伪造
                .csrf().disable()
                // 开启跨域以便前端调用接口
                .cors().and()
                .authorizeRequests()
                // 指定某些接口不需要通过验证即可访问。登陆接口肯定是不需要认证的
                .antMatchers("/admin/system/index/login").permitAll()
                // 这里意思是其它所有接口需要认证才能访问
                .anyRequest().authenticated()
                .and()
                //TokenAuthenticationFilter放到UsernamePasswordAuthenticationFilter的前面，这样做就是为了除了登录的时候去查询数据库外，其他时候都用token进行认证。
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new TokenLoginFilter(authenticationManager(), redisTemplate));

        //禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定UserDetailService和加密器
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(customMd5PasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * 排除swagger相关请求
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/admin/modeler/**","/diagram-viewer/**","/editor-app/**","/*.html",
                "/admin/processImage/**",
                "/admin/wechat/authorize","/admin/wechat/userInfo","/admin/wechat/bindPhone",
                "/favicon.ico","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html");
    }
}
```
**4.4.2用户授权**<br>
在SpringSecurity中，会使用默认的FilterSecurityInterceptor来进行权限校验。在FilterSecurityInterceptor中会从SecurityContextHolder获取其中的**Authentication**，然后获取其中的权限信息。判断当前用户是否拥有访问当前资源所需的权限。<br>
在TokenLoginFilter中登录成功时，我们把权限数据保存到redis中（用户名为key，权限数据为value），已经登入的情况下在TokenAuthenticationFilter中通过token获取用户名再通过redis即可拿到权限数据，这样就可构成出完整的Authentication对象。<br>
**Spring Security默认是禁用注解的，要想开启注解，需要在继承WebSecurityConfigurerAdapter的类上加@EnableGlobalMethodSecurity注解，来判断用户对某个控制层的方法是否具有访问权限**<br>
通过@PreAuthorize标签控制controller层接口权限，例如：<br>
```Java
 @PreAuthorize("hasAuthority('bnt.sysRole.list')")
 @ApiOperation(value = "获取")
 @GetMapping("get/{id}")
 public Result get(@PathVariable Long id) {
     SysRole role = sysRoleService.getById(id);
     return Result.ok(role);
 }

 @PreAuthorize("hasAuthority('bnt.sysRole.add')")
 @ApiOperation(value = "新增角色")
 @PostMapping("save")
 public Result save(@RequestBody @Validated SysRole role) {
     sysRoleService.save(role);
     return Result.ok();   
```
只有拥有该权限才能访问该Controller
### 5.审批设置
审批设置模块包含：审批类型与审批模板
审批类型：审批类型即为审批的分类，如：出勤、人事、财务等
审批模板：设置具体审批的基本信息、表单信息与审批流程定义，审批流涉及工作流引擎Activiti，常见的审批模板如：加班、出差、请假、费用报销等，我们可以根据公司具体业务配置具体的审批模板
**管理端页面效果**
1. 审批类型<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval-type.jpg)<br>
2. 审批模板<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval-template.jpg)<br>
3. 在线流程设计<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/process-design.jpg)<br>
**员工端页面效果**
1. 审批中心<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval-center.jpg)<br>
2. 发起审批，显示动态表单<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval-form.jpg)<br>
**对于审批模板中，审批流程的设计是使用activiti，具体参考 /参考资料/activiti.md文件；对于审批类型和模块的增删改查具体的后端代码，存放于service-co模块的com.wu.process包下ProcessTypeController.java和ProcessTemplateController**<br>
### 6.审批管理
审批管理是是在管理端查看和处理员工申请的审批流程，对应的后端代码在service-co模块的com.wu.process包下的ProcessController.java<br>
管理端页面效果<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval-manager.jpg)<br>
### 7.审批流程的申请和处理
通过前端项目wu-co-web发起审批流程的申请和处理，后期将该前端页面整合到微信公众中。<br>
（本地测试阶段需将wu-co-web项目中的utils/request.js中的baseURL改为http://localhost:8800；App.vue中将create方法内容注释掉；在/views中添加test.vue,再里面进行给进行token的添加和用户切换）<br>
前端（测试）：<br>
首先访问http://localhost:9090/#/test，获取token，有token才能是登入状态，才能访问<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/test-vue.jpg)<br>
再访问http://localhost:9090/#/，进行审批的申请，测试时页面在本地浏览器显示所以比例不对，后期是在微信端显示。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/apply.jpg)<br>
访问http://localhost:9090/#/list/1,http://localhost:9090/#/list/2,http://localhost:9090/#/list/3分别为已处理审批，已发起审批，待处理审批。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/processed-approval.jpg)<br>
后端实现，实现代码在service-co模块的com.wu.process.api包下ProcessApiController.java中，后端实现流程如图：<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/approval-process.jpg)<br>
### 8.整合微信公众号
审批流程的申请和处理在7中只是在本地实现了相关功能，要实现在微信端进行审批流程的申请和处理还需进行一下几步。<br>
1. 申请公众号<br>
2. 实现管理端中微信菜单的增删改查和同步到微信中，后端同步菜单使用weixin-java-mp工具。<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/wechat-menu.jpg)<br>
3. 微信端使用内网穿透对员工端前端页面wu-co-web进行访问，和对后端Controller进行访问，流程如图<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/inner.jpg)<br>
4. 后端中需要实现微信的登入验证<br>
![](https://github.com/wuchuimao/CloudOffice/raw/master/images/wechat-login.jpg)<br>
**具体设置参考文件 /参考资料/微信公众号.md**
